package ru.mail.confluence.plugins.utils.spacevariables;

import com.atlassian.confluence.content.render.image.ImageDimensions;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.DefaultImagePlaceholder;
import com.atlassian.confluence.macro.EditorImagePlaceholder;
import com.atlassian.confluence.macro.ImagePlaceholder;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.macro.ResourceAware;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import ru.mail.confluence.plugins.utils.common.Consts;

import java.util.Map;

public class SpaceVariableMacro implements Macro, EditorImagePlaceholder, ResourceAware {
    private static final String PLACEHOLDER_SERVLET = "/plugins/servlet/confluence-utils/space-variables-macro/placeholder";
    private static final int IMG_WIDTH = 88;
    private static final int IMG_HEIGHT = 18;

    private final PageManager pageManager;
    private final SpaceVariableManager spaceVariableManager;

    public SpaceVariableMacro(PageManager pageManager, SpaceVariableManager spaceVariableManager) {
        this.pageManager = pageManager;
        this.spaceVariableManager = spaceVariableManager;
    }

    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> velocityContext = MacroUtils.defaultVelocityContext();
        SpaceVariable spaceVariable = null;
        String id = map.get(Consts.SPACE_VARIABLES_MACRO_ID_PARAMETER);
        if (id != null) {
            spaceVariable = spaceVariableManager.getVariable(Integer.parseInt(id));
            Page page = pageManager.getPage(spaceVariable.getPageId());
            if (page != null)
                velocityContext.put("url", page.getUrlPath());
        }
        velocityContext.put("spaceVariable", spaceVariable);
        return VelocityUtils.getRenderedTemplate("/ru/mail/confluence/plugins/utils/spacevariables/space-variables-macro.vm", velocityContext);
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.INLINE;
    }

    @Override
    public ImagePlaceholder getImagePlaceholder(Map<String, String> params, ConversionContext conversionContext) {
        String id="";
        if (params.containsKey(Consts.SPACE_VARIABLES_MACRO_ID_PARAMETER))
            id = params.get(Consts.SPACE_VARIABLES_MACRO_ID_PARAMETER);
        return new DefaultImagePlaceholder(String.format("%s?%s=%s", PLACEHOLDER_SERVLET, Consts.SPACE_VARIABLES_MACRO_ID_PARAMETER, id), false, new ImageDimensions(IMG_WIDTH, IMG_HEIGHT));
    }

    @Override
    public String getResourcePath() {
        return "";
    }

    @Override
    public void setResourcePath(String s) {
    }
}
