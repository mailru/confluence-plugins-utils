package ru.mail.confluence.plugins.utils.spacevariables;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.spaces.actions.SpaceAdminAction;
import com.atlassian.confluence.user.ConfluenceUser;
import org.apache.commons.lang3.StringUtils;
import ru.mail.jira.plugins.commons.RestExecutor;
import ru.mail.jira.plugins.commons.RestFieldException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/spacevariable")
@Produces({MediaType.APPLICATION_JSON})
public class ConfigureSpaceVariablesAction extends SpaceAdminAction {
    private final PageManager pageManager;
    private final PermissionManager permissionManager;
    private final SpaceManager spaceManager;
    private final SpaceVariableManager spaceVariableManager;

    public ConfigureSpaceVariablesAction(PageManager pageManager, PermissionManager permissionManager, SpaceManager spaceManager, SpaceVariableManager spaceVariableManager) {
        this.pageManager = pageManager;
        this.permissionManager = permissionManager;
        this.spaceManager = spaceManager;
        this.spaceVariableManager = spaceVariableManager;
    }

    private void checkRequireFields(String name, ConfluencePageDto page) {
        if (StringUtils.trimToNull(name) == null)
            throw new RestFieldException(getI18n().getText("required.field"), "name");
        if (page == null)
            throw new RestFieldException(getI18n().getText("required.field"), "page");
    }

    private boolean isSpaceAdmin(Space space) {
        return permissionManager.hasPermission(getAuthenticatedUser(), Permission.ADMINISTER, space);
    }

    @GET
    @Path("/all")
    public Response getAllSpaceVariables() {
        return new RestExecutor<List<SpaceVariableDto>>() {
            @Override
            protected List<SpaceVariableDto> doAction() throws Exception {
                List<SpaceVariableDto> result = new ArrayList<SpaceVariableDto>();
                for (SpaceVariable spaceVariable : spaceVariableManager.getVariables()) {
                    SpaceVariableDto spaceVariableDto = new SpaceVariableDto(spaceVariable);
                    Page page = pageManager.getPage(spaceVariable.getPageId());
                    if (page != null)
                        spaceVariableDto.setPage(new ConfluencePageDto(page.getId(), page.getTitle(), page.getUrlPath(), page.getSpaceKey(), page.getSpace().getName()));
                    result.add(spaceVariableDto);
                }
                return result;
            }
        }.getResponse();
    }

    @GET
    public Response searchSpaceVariables(@QueryParam("spaceKey") final String spaceKey,
                                         @QueryParam("filter") final String filter,
                                         @QueryParam("limit") final int limit) {
        return new RestExecutor<List<SpaceVariableDto>>() {
            @Override
            protected List<SpaceVariableDto> doAction() throws Exception {
                List<SpaceVariableDto> result = new ArrayList<SpaceVariableDto>();
                for (SpaceVariable spaceVariable : spaceVariableManager.searchVariables(spaceManager.getSpace(spaceKey).getId(), filter, limit)) {
                    SpaceVariableDto spaceVariableDto = new SpaceVariableDto(spaceVariable);
                    Page page = pageManager.getPage(spaceVariable.getPageId());
                    if (page != null)
                        spaceVariableDto.setPage(new ConfluencePageDto(page.getId(), page.getTitle(), page.getUrlPath(), page.getSpaceKey(), page.getSpace().getName()));
                    result.add(spaceVariableDto);
                }
                return result;
            }
        }.getResponse();
    }

    @GET
    @Path("{id}")
    public Response getSpaceVariable(@PathParam("id") final int id) {
        return new RestExecutor<SpaceVariableDto>() {
            @Override
            protected SpaceVariableDto doAction() throws Exception {
                SpaceVariable spaceVariable = spaceVariableManager.getVariable(id);
                SpaceVariableDto spaceVariableDto = new SpaceVariableDto(spaceVariable);
                Page page = pageManager.getPage(spaceVariable.getPageId());
                if (page != null)
                    spaceVariableDto.setPage(new ConfluencePageDto(page.getId(), page.getTitle(), page.getUrlPath(), page.getSpaceKey(), page.getSpace().getName()));
                return spaceVariableDto;
            }
        }.getResponse();
    }

    @POST
    public Response createSpaceVariable(final SpaceVariableDto spaceVariableDto) {
        return new RestExecutor<SpaceVariableDto>() {
            @Override
            protected SpaceVariableDto doAction() throws Exception {
                Space space = spaceManager.getSpace(spaceVariableDto.getSpaceKey());
                if (!isSpaceAdmin(space))
                    throw new SecurityException();
                checkRequireFields(spaceVariableDto.getName(), spaceVariableDto.getPage());

                SpaceVariable spaceVariable = spaceVariableManager.createVariable(spaceVariableDto.getName(), spaceVariableDto.getPage().getId(), spaceVariableDto.getDescription(), space.getId());
                SpaceVariableDto spaceVariableDto = new SpaceVariableDto(spaceVariable);
                Page page = pageManager.getPage(spaceVariable.getPageId());
                if (page != null)
                    spaceVariableDto.setPage(new ConfluencePageDto(page.getId(), page.getTitle(), page.getUrlPath(), page.getSpaceKey(), page.getSpace().getName()));
                return spaceVariableDto;
            }
        }.getResponse();
    }

    @PUT
    @Path("{id}")
    public Response updateSpaceVariable(final SpaceVariableDto spaceVariableDto) {
        return new RestExecutor<SpaceVariableDto>() {
            @Override
            protected SpaceVariableDto doAction() throws Exception {
                Space space = spaceManager.getSpace(spaceVariableDto.getSpaceKey());
                if (!isSpaceAdmin(space))
                    throw new SecurityException();
                checkRequireFields(spaceVariableDto.getName(), spaceVariableDto.getPage());

                SpaceVariable spaceVariable = spaceVariableManager.updateVariable(spaceVariableDto.getId(), spaceVariableDto.getName(), spaceVariableDto.getPage().getId(), spaceVariableDto.getDescription(), space.getId());
                SpaceVariableDto spaceVariableDto = new SpaceVariableDto(spaceVariable);
                Page page = pageManager.getPage(spaceVariable.getPageId());
                if (page != null)
                    spaceVariableDto.setPage(new ConfluencePageDto(page.getId(), page.getTitle(), page.getUrlPath(), page.getSpaceKey(), page.getSpace().getName()));
                return spaceVariableDto;
            }
        }.getResponse();
    }


    @DELETE
    @Path("{id}")
    public Response deleteSpaceVariable(@PathParam("id") final int id) {
        return new RestExecutor<Void>() {
            @Override
            protected Void doAction() throws Exception {
                Space space = spaceManager.getSpace(spaceVariableManager.getVariable(id).getSpaceId());
                if (!isSpaceAdmin(space))
                    throw new SecurityException();

                spaceVariableManager.deleteVariable(id);
                return null;
            }
        }.getResponse();
    }

    @GET
    @Path("/pages")
    public Response getPages(@QueryParam("key") final String key,
                             @QueryParam("filter") final String filter) {
        return new RestExecutor<List<ConfluencePageDto>>() {
            @Override
            protected List<ConfluencePageDto> doAction() throws Exception {
                List<ConfluencePageDto> result = new ArrayList<ConfluencePageDto>();
                ConfluenceUser confluenceUser = getAuthenticatedUser();

                String formattedFilter = filter.trim().toLowerCase();
                Space space = spaceManager.getSpace(key);

                for (Page page: pageManager.getPages(space, true))
                    if (permissionManager.hasPermission(confluenceUser, Permission.VIEW, page)) {
                        if (result.size() >= 10)
                            break;

                        if (StringUtils.isEmpty(formattedFilter)) {
                            result.add(new ConfluencePageDto(page.getId(), page.getTitle(), page.getUrlPath(), page.getSpaceKey(), page.getSpace().getName()));
                            continue;
                        }
                        if (StringUtils.containsIgnoreCase(page.getTitle(), formattedFilter) || StringUtils.containsIgnoreCase(formattedFilter, page.getUrlPath()))
                            result.add(new ConfluencePageDto(page.getId(), page.getTitle(), page.getUrlPath(), page.getSpaceKey(), page.getSpace().getName()));
                    }
                return result;
            }
        }.getResponse();
    }
}
