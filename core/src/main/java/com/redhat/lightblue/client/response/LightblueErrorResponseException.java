/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.response;

/**
 * Exception thrown when an error status is returned from Lightblue.
 *
 * @author dcrissman
 */
public class LightblueErrorResponseException extends RuntimeException {

    private static final long serialVersionUID = -3732433923527169586L;

    public LightblueErrorResponseException(String message) {
        super(message);
    }

    public LightblueErrorResponseException(Throwable cause) {
        super(cause);
    }

    public LightblueErrorResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}
