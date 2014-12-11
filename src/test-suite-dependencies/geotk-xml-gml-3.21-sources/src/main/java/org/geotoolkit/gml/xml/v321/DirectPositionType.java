/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008 - 2012, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */


package org.geotoolkit.gml.xml.v321;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import org.geotoolkit.metadata.iso.citation.Citations;
import org.geotoolkit.referencing.CRS;
import org.geotoolkit.referencing.IdentifiedObjects;
import org.geotoolkit.util.Utilities;
import org.geotoolkit.util.logging.Logging;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;


/**
 * Direct position instances hold the coordinates for a position within some coordinate reference system (CRS). Since direct positions, as data types, will often be included in larger objects (such as geometry elements) that have references to CRS, the srsName attribute will in general be missing, if this particular direct position is included in a larger element with such a reference to a CRS. In this case, the CRS is implicitly assumed to take on the value of the containing object's CRS.
 * if no srsName attribute is given, the CRS shall be specified as part of the larger context this geometry element is part of, typically a geometric object like a point, curve, etc.
 * 
 * <p>Java class for DirectPositionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectPositionType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.opengis.net/gml/3.2>doubleList">
 *       &lt;attGroup ref="{http://www.opengis.net/gml/3.2}SRSReferenceGroup"/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectPositionType", propOrder = {
    "value"
})
@XmlSeeAlso({
    VectorType.class
})
public class DirectPositionType implements org.geotoolkit.gml.xml.DirectPosition {

    @XmlValue
    private List<Double> value;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String srsName;
    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    private Integer srsDimension;
    @XmlAttribute
    private List<String> axisLabels;
    @XmlAttribute
    private List<String> uomLabels;

    /**
     * Empty constructor used by JAXB.
     */
    DirectPositionType() {}

    /**
     * Build a full Direct position.
     * @param srsName
     * @param srsDimension
     * @param axisLabels
     * @param value
     * @param uomLabels
     */
    public DirectPositionType(final String srsName, final Integer srsDimension, final List<String> axisLabels,
            final List<Double> value, final List<String> uomLabels)
    {
        this.srsName      = srsName;
        this.srsDimension = srsDimension;
        this.axisLabels   = axisLabels;
        this.value        = value;
        this.uomLabels    = uomLabels;
    }

    /**
     * Build a full Direct position.
     * @param srsName
     * @param srsDimension
     * @param axisLabels
     * @param value
     */
    public DirectPositionType(final String srsName, final Integer srsDimension, final List<String> axisLabels,
            final List<Double> value)
    {
        this.srsName      = srsName;
        this.srsDimension = srsDimension;
        this.axisLabels   = axisLabels;
        this.value        = value;
    }

    /**
     * Build a full Direct position.
     * @param srsName
     * @param srsDimension
     * @param value
     */
    public DirectPositionType(final String srsName, final Integer srsDimension, final List<Double> value) {
        this.srsName      = srsName;
        this.srsDimension = srsDimension;
        this.value        = value;
    }

    /**
     * Build a light direct position.
     *
     * @param
     * @param value a List of coordinates.
     */
    public DirectPositionType(final List<Double> value) {
        this.value        = value;
        this.srsDimension = null;
    }

    /**
     * Build a light direct position.
     *
     * @param values a List of coordinates.
     */
    public DirectPositionType(final double... values) {
        this.value = new ArrayList<Double>();
        for (Double pt: values) {
            if (pt != null && !pt.equals(Double.NaN)) {
                this.value.add(pt);
            }
        }
        this.srsDimension = null;
    }

    /**
     * Build a light direct position.
     *
     * @param values a List of coordinates.
     */
    public DirectPositionType(final DirectPosition position, final boolean srsInfo) {
        if (position != null) {
            this.value = new ArrayList<Double>();
            for (double d : position.getCoordinate()) {
                value.add(d);
            }
            if (srsInfo) {
                CoordinateReferenceSystem crs = position.getCoordinateReferenceSystem();
                if ( crs != null) {
                    try {
                        this.srsName = IdentifiedObjects.lookupIdentifier(Citations.URN_OGC, crs, true);
                    } catch (FactoryException ex) {
                        Logging.getLogger(DirectPositionType.class).log(Level.WARNING, null, ex);
                    }
                }
                this.srsDimension = position.getDimension();
            }
        }
    }
    
    /**
     * Build a light direct position.
     *
     * @param values a List of coordinates.
     */
    public DirectPositionType(final org.geotoolkit.gml.xml.DirectPosition position) {
        if (position != null) {
            this.axisLabels   = position.getAxisLabels();
            this.srsDimension = position.getSrsDimension();
            this.srsName      = position.getSrsName();
            this.uomLabels    = position.getUomLabels();
            this.value        = position.getValue();
        }
    }

    
    /**
     * A type for a list of values of the respective simple type.Gets the value of the value property.
     * 
     */
    @Override
    public List<Double> getValue() {
        if (value == null) {
            value = new ArrayList<Double>();
        }
        return this.value;
    }

    /**
     * Gets the value of the srsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
    public String getSrsName() {
        return srsName;
    }

    /**
     * Sets the value of the srsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrsName(String value) {
        this.srsName = value;
    }

    /**
     * Gets the value of the srsDimension property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    @Override
    public Integer getSrsDimension() {
        return srsDimension;
    }

    /**
     * Sets the value of the srsDimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSrsDimension(Integer value) {
        this.srsDimension = value;
    }

    /**
     * Gets the value of the axisLabels property.
     * 
     */
    @Override
    public List<String> getAxisLabels() {
        if (axisLabels == null) {
            axisLabels = new ArrayList<String>();
        }
        return this.axisLabels;
    }

    /**
     * Gets the value of the uomLabels property.
     * 
     */
    @Override
    public List<String> getUomLabels() {
        if (uomLabels == null) {
            uomLabels = new ArrayList<String>();
        }
        return this.uomLabels;
    }
    
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if (srsName != null) {
            try {
                return CRS.decode(srsName);
            } catch (NoSuchAuthorityCodeException ex) {
                Logging.getLogger(org.geotoolkit.gml.xml.v311.DirectPositionType.class).log(Level.WARNING, null, ex);
            } catch (FactoryException ex) {
                Logging.getLogger(org.geotoolkit.gml.xml.v311.DirectPositionType.class).log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    @Override
    public int getDimension() {
        if (srsDimension != null) {
            return srsDimension.intValue();
        }
        return value.size();
    }

    @Override
    public double[] getCoordinate() {
        double[] coords = new double[value.size()];
        for(int i=0,n=value.size(); i<n; i++){
            coords[i] = value.get(i);
        }
        return coords;
    }

    @Override
    public double getOrdinate(final int dimension) throws IndexOutOfBoundsException {
        return value.get(dimension);
    }

    @Override
    public void setOrdinate(final int dimension, final double value) throws IndexOutOfBoundsException, UnsupportedOperationException {
        this.value.remove(dimension);
        this.value.add(dimension, value);
    }

    @Override
    public DirectPosition getDirectPosition() {
        return this;
    }

    /**
     * Return a description of the object.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[DirectPositionType]:");
        if (srsName != null) {
            s.append("srsName = ").append(srsName).append('\n');
        }
        if (srsDimension != null) {
            s.append(" srsDimension = ").append(srsDimension).append('\n');
        }
        if (value != null) {
            s.append(" value: ").append('\n');
            for(double v :value) {
                s.append(v).append(", ");
            }
        }
        return s.toString();
    }

    /**
     * Verify that this entry is identical to the specified object.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DirectPositionType) {
            final DirectPositionType that = (DirectPositionType) object;
            return  Utilities.equals(this.getAxisLabels(), that.getAxisLabels()) &&
                    Utilities.equals(this.srsDimension,    that.srsDimension)    &&
                    Utilities.equals(this.srsName,         that.srsName)         &&
                    Utilities.equals(this.getUomLabels(),  that.getUomLabels())  &&
                    Utilities.equals(this.value,           that.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 71 * hash + (this.srsDimension != null ? this.srsDimension.intValue() : 0);
        hash = 71 * hash + (this.srsName != null ? this.srsName.hashCode() : 0);
        hash = 71 * hash + (this.axisLabels != null ? this.axisLabels.hashCode() : 0);
        hash = 71 * hash + (this.uomLabels != null ? this.uomLabels.hashCode() : 0);
        return hash;
    }
}