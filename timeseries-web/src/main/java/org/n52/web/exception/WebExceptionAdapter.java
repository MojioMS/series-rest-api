/**
 * Copyright (C) 2013-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.web.exception;

import org.n52.io.request.IoParameters;
import org.n52.io.response.OutputCollection;
import org.n52.sensorweb.spi.ParameterService;

/**
 * Adapts SPI exceptions to HTTP specified Web exceptions.
 *
 * @author Henning Bredel <h.bredel@52north.org>
 *
 * @param <T> the parameter type of the service to adapt execptions for.
 */
public class WebExceptionAdapter<T> implements ParameterService<T> {

    private final ParameterService<T> composedService;

    public WebExceptionAdapter(ParameterService<T> toCompose) {
        this.composedService = toCompose;
    }

    @Override
    public OutputCollection<T> getExpandedParameters(IoParameters query) {
        OutputCollection<T> parameters = composedService.getExpandedParameters(query);
        if (parameters == null) {
            throw new InternalServerException("SPI implementation did return null value!");
        }
        return parameters;
    }

    @Override
    public OutputCollection<T> getCondensedParameters(IoParameters query) {
        OutputCollection<T> parameters = composedService.getCondensedParameters(query);
        if (parameters == null) {
            throw new InternalServerException("SPI implementation did return null value!");
        }
        return parameters;
    }

    @Override
    public OutputCollection<T> getParameters(String[] items) {
        OutputCollection<T> parameters = composedService.getParameters(items);
        if (parameters == null) {
            throw new InternalServerException("SPI implementation did return null value!");
        }
        return parameters;
    }

    @Override
    public OutputCollection<T> getParameters(String[] items, IoParameters query) {
        OutputCollection<T> parameters = composedService.getParameters(items, query);
        if (parameters == null) {
            throw new InternalServerException("SPI implementation did return null value!");
        }
        return parameters;
    }

    @Override
    public T getParameter(String item) {
        T parameter = composedService.getParameter(item);
        if (parameter == null) {
            throw new ResourceNotFoundException("Resource with id '" + item + "' was not found.");
        }
        return parameter;
    }

    @Override
    public T getParameter(String item, IoParameters query) {
        T parameter = composedService.getParameter(item, query);
        if (parameter == null) {
            throw new ResourceNotFoundException("Resource with id '" + item + "' was not found.");
        }
        return parameter;
    }

}