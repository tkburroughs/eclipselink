/*
 * Copyright (c) 1998, 2024 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     01/28/2009-2.0 Guy Pelletier
//       - 248293: JPA 2.0 Element Collections (part 1)
//     03/27/2009-2.0 Guy Pelletier
//       - 241413: JPA 2.0 Add EclipseLink support for Map type attributes
package org.eclipse.persistence.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The MapKeyConvert annotation specifies that a named converter should be used
 * with the corresponding mapped attribute key column. The MapKeyConvert
 * annotation has the following reserved names:
 * <ul>
 *  <li>{@value Convert#SERIALIZED}: Uses a {@linkplain org.eclipse.persistence.mappings.converters.SerializedObjectConverter}
 *  on the associated mapping. When using a {@linkplain org.eclipse.persistence.mappings.converters.SerializedObjectConverter}
 *  the database representation is a binary field holding a serialized version of the object and the object-model representation is a the
 *  actual object.</li>
 *  <li>{@value Convert#CLASS_INSTANCE}: Uses an {@linkplain org.eclipse.persistence.mappings.converters.ClassInstanceConverter}
 *  on the associated mapping. When using a {@linkplain org.eclipse.persistence.mappings.converters.ClassInstanceConverter}
 *  the database representation is a String representing the Class name and the object-model representation is an instance
 *  of that class built with a no-args constructor.</li>
 *  <li>{@value Convert#XML}: Uses an {@linkplain org.eclipse.persistence.mappings.converters.SerializedObjectConverter}
 *  with the {@linkplain org.eclipse.persistence.sessions.serializers.XMLSerializer} on the associated mapping.
 *  When using a {@linkplain org.eclipse.persistence.sessions.serializers.XMLSerializer} the database representation is a
 *  character field holding a serialized version of the object and the object-model representation is a the actual object.</li>
 *  <li>{@value Convert#JSON}: Uses an {@linkplain org.eclipse.persistence.mappings.converters.SerializedObjectConverter}
 *  with the {@linkplain org.eclipse.persistence.sessions.serializers.JSONSerializer} on the associated mapping.
 *  When using a {@linkplain org.eclipse.persistence.sessions.serializers.JSONSerializer} the database representation is a
 *  character field holding a serialized version of the object and the object-model representation is a the actual object.</li>
 *  <li>{@value Convert#KRYO}: Uses an {@linkplain org.eclipse.persistence.mappings.converters.SerializedObjectConverter}
 *  with the org.eclipse.persistence.sessions.serializers.kryo.KryoSerializer on the associated mapping.
 *  When using a org.eclipse.persistence.sessions.serializers.kryo.KryoSerializer the database representation is a
 *  binary field holding a serialized version of the object and the object-model representation is a the actual object.</li>
 *  <li>{@value Convert#NONE}: Places no converter on the associated mapping. This can be used to override a situation where either
 *  another converter is defaulted or another converter is set.</li>
 * </ul>
 * <p>
 * When these reserved converters are not used, it is necessary to define a converter to use using the {@linkplain Converter} annotation.
 *
 * @see Converter
 * @see ObjectTypeConverter
 * @see TypeConverter
 * @see org.eclipse.persistence.mappings.converters.SerializedObjectConverter
 * @see org.eclipse.persistence.mappings.converters.ClassInstanceConverter
 *
 * @author Guy Pelletier
 * @since EclipseLink 1.2
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface MapKeyConvert {
    /**
     * The name of the converter to be used.
     */
    String value() default Convert.NONE;
}
