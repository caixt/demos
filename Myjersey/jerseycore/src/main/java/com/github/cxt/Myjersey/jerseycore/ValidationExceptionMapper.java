package com.github.cxt.Myjersey.jerseycore;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.ExceptionMapper;
import javax.inject.Provider;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.internal.LocalizationMessages;
import org.glassfish.jersey.server.validation.ValidationError;
import org.glassfish.jersey.server.validation.internal.ValidationHelper;
import org.springframework.web.context.WebApplicationContext;

@javax.ws.rs.ext.Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
	private static final Logger LOGGER = Logger.getLogger(ValidationExceptionMapper.class.getName());
	
	private static final String CONTEXT_ATTRIBUTE = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

    @Context
    private Configuration config;
    @Context
    private Provider<Request> request;
    @Context
    private ServletContext servletContext;
    @Context
    private HttpServletRequest servletrequest;

    @Override
    public Response toResponse(final ValidationException exception) {
    	System.out.println(this + "!" + request);
    	WebApplicationContext context = (WebApplicationContext) servletContext
    			.getAttribute(CONTEXT_ATTRIBUTE);
    	System.out.println(servletrequest.getLocale());
    	System.out.println(context.getMessage("test", null, servletrequest.getLocale()));
        if (exception instanceof ConstraintViolationException) {
            LOGGER.log(Level.FINER, LocalizationMessages.CONSTRAINT_VIOLATIONS_ENCOUNTERED(), exception);

            final ConstraintViolationException cve = (ConstraintViolationException) exception;
            final Response.ResponseBuilder response = Response.status(ValidationHelper.getResponseStatus(cve));

            // Entity.
            final Object property = config.getProperty(ServerProperties.BV_SEND_ERROR_IN_RESPONSE);
            if (property != null && Boolean.valueOf(property.toString())) {
                final List<Variant> variants = Variant.mediaTypes(
                        //MediaType.TEXT_PLAIN_TYPE,
                        //MediaType.TEXT_HTML_TYPE,
                        //MediaType.APPLICATION_XML_TYPE,
                        MediaType.APPLICATION_JSON_TYPE).build();
                final Variant variant = request.get().selectVariant(variants);
                if (variant != null) {
                    response.type(variant.getMediaType());
                } else {

                    // default media type which will be used only when none media type from {@value variants} is in accept
                    // header of original request.
                    // could be settable by configuration property.
                    response.type(MediaType.TEXT_PLAIN_TYPE);
                }
                response.entity(
                        new GenericEntity<>(
                                ValidationHelper.constraintViolationToValidationErrors(cve),
                                new GenericType<List<ValidationError>>() {}.getType()
                        )
                );
            }

            return response.build();
        } else {
            LOGGER.log(Level.WARNING, LocalizationMessages.VALIDATION_EXCEPTION_RAISED(), exception);

            return Response.serverError().entity(exception.getMessage()).build();
        }
    }
}