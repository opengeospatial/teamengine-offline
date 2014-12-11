/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2004-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2012, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.referencing.factory.web;

import net.jcip.annotations.Immutable;
import org.opengis.parameter.ParameterValueGroup;


/**
 * Auto Mollweide ({@code AUTO:42005}).
 * In the notation below, "<code>${var}</code>" denotes a reference to the value of a variable
 * "{@code var}". The variables "{@code lat0}" and "{@code lon0}" are the central point of the
 * projection appearing in the CRS parameter of the map request.
 *
 * {@preformat wkt
 *   PROJCS["WGS 84 / Auto Mollweide",
 *     GEOGCS["WGS 84",
 *       DATUM["WGS_1984",
 *         SPHEROID["WGS_1984", 6378137, 298.257223563]],
 *       PRIMEM["Greenwich", 0],
 *       UNIT["Decimal_Degree", 0.0174532925199433]],
 *     PROJECTION["Mollweide"],
 *     PARAMETER["Central_Meridian", ${central_meridian}],
 *     UNIT["Meter", 1]]
 * }
 *
 * Where:
 *
 * {@preformat text
 *   ${central_meridian} = ${lon0}
 * }
 *
 * @author Jody Garnett (Refractions)
 * @author Rueben Schulz (UBC)
 * @author Martin Desruisseaux (IRD)
 * @version 3.00
 *
 * @since 2.2
 * @module
 */
@Immutable
final class Auto42005 extends Factlet {
    /**
     * A shared (thread-safe) instance.
     */
    public static final Auto42005 DEFAULT = new Auto42005();

    /**
     * Do not allows instantiation except the {@link #DEFAULT} constant.
     */
    private Auto42005() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int code() {
        return 42005;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "WGS 84 / Auto Mollweider";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassification() {
        return "Mollweide";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setProjectionParameters(final ParameterValueGroup parameters, final Code code) {
        final double centralMeridian = code.longitude;
        parameters.parameter("central_meridian").setValue(centralMeridian);
    }
}
