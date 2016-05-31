package ru.mail.confluence.plugins.confluenceutils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.jira.plugins.commons.RestFieldException;
import ru.mail.jira.plugins.commons.StreamRestResult;

import javax.ws.rs.core.Response;

public abstract class RestExecutor<T> {
    private static final String FIELD_HEADER = "X-Atlassian-Rest-Exception-Field";
    private static final Logger log = LoggerFactory.getLogger(RestExecutor.class);

    public RestExecutor() {
    }

    protected abstract T doAction() throws Exception;

    public Response getResponse() {
        return this.getResponse(Response.Status.OK);
    }

    public Response getResponse(Response.Status successStatus) {
        Response.ResponseBuilder responseBuilder;
        try {
            Object e = this.doAction();
            responseBuilder = Response.status(successStatus).entity(e);
            if (e instanceof byte[]) {
                responseBuilder = responseBuilder.type("application/force-download").header("Content-Transfer-Encoding", "binary").header("charset", "UTF-8");
            } else if (e instanceof StreamRestResult) {
                responseBuilder = responseBuilder.entity(((StreamRestResult) e).getInputStream()).type(((StreamRestResult) e).getContentType());
            }

            return responseBuilder.build();
        } catch (SecurityException var4) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (IllegalArgumentException var5) {
            responseBuilder = Response.status(Response.Status.BAD_REQUEST).entity(var5.getMessage());
            if (var5 instanceof RestFieldException) {
                responseBuilder = responseBuilder.header(FIELD_HEADER, ((RestFieldException) var5).getField());
            }
            return responseBuilder.build();
        } catch (Exception var6) {
            log.error("REST Exception", var6);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(var6.getMessage()).build();
        }
    }
}
