// $Header$
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
 lat/lon Fitzke/Fretter/Poth GbR
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon Fitzke/Fretter/Poth GbR
 Meckenheimer Allee 176
 53115 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Jens Fitzke
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: jens.fitzke@uni-bonn.de


 ---------------------------------------------------------------------------*/
package org.opengis.webservice;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 */
public final class ExceptionCode extends CodeList<ExceptionCode> {
    /**
     * Serial number for compatibility with different versions.
     */
//    private static final long serialVersionUID = **TODO**;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<ExceptionCode> VALUES = new ArrayList<ExceptionCode>(8);

    /**
     *
     */
    @UML(identifier="InvalidFormat", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode INVALID_FORMAT = new ExceptionCode("INVALID_FORMAT");

    /**
     *
     */
    @UML(identifier="CurrentUpdateSequence", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode CURRENT_UPDATE_SEQUENCE = new ExceptionCode("CURRENT_UPDATE_SEQUENCE");

    /**
     *
     */
    @UML(identifier="InvalidUpdateSequence", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode INVALID_UPDATE_SEQUENCE = new ExceptionCode("INVALID_UPDATE_SEQUENCE");

    /**
     *
     */
    @UML(identifier="MissingParameterValue", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode MISSING_PARAMETER_VALUE = new ExceptionCode("MISSING_PARAMETER_VALUE");

    /**
     *
     */
    @UML(identifier="InvalidParameterValue", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode INVALID_PARAMETER_VALUE = new ExceptionCode("INVALID_PARAMETER_VALUE");

    /**
     *
     */
    @UML(identifier="OperationNotSupported", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode OPERATION_NOT_SUPPORTED = new ExceptionCode("OPERATION_NOT_SUPPORTED");

    /**
     *
     */
    @UML(identifier="VersionNegotiationFailed", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode VERSION_NEGOTIATION_FAILED = new ExceptionCode("VERSION_NEGOTIATION_FAILED");

    /**
     *
     */
    @UML(identifier="NoApplicableCode", obligation=CONDITIONAL, specification=UNSPECIFIED)
    public static final ExceptionCode NO_APPLICABLE_CODE = new ExceptionCode("NO_APPLICABLE_CODE");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private ExceptionCode(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code ExceptionCode}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static ExceptionCode[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new ExceptionCode[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public ExceptionCode[] family() {
        return values();
    }

    /**
     * Returns the exception code that matches the given string, or returns a
     * new one if none match it. More specifically, this methods returns the first instance for
     * which <code>{@linkplain #name() name()}.{@linkplain String#equals equals}(code)</code>
     * returns {@code true}. If no existing instance is found, then a new one is created for
     * the given name.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static ExceptionCode valueOf(String code) {
        return valueOf(ExceptionCode.class, code);
    }
}
