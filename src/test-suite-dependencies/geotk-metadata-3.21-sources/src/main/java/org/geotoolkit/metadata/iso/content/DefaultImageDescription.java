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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotoolkit.metadata.iso.content;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.jcip.annotations.ThreadSafe;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.content.ImageDescription;
import org.opengis.metadata.content.ImagingCondition;

import org.geotoolkit.lang.ValueRange;


/**
 * Information about an image's suitability for use.
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @author Touraïvane (IRD)
 * @author Cédric Briançon (Geomatys)
 * @version 3.19
 *
 * @since 2.1
 * @module
 */
@ThreadSafe
@XmlType(name = "MD_ImageDescription_Type", propOrder={
    "illuminationElevationAngle",
    "illuminationAzimuthAngle",
    "imagingCondition",
    "imageQualityCode",
    "cloudCoverPercentage",
    "processingLevelCode",
    "compressionGenerationQuantity",
    "triangulationIndicator",
    "radiometricCalibrationDataAvailable",
    "cameraCalibrationInformationAvailable",
    "filmDistortionInformationAvailable",
    "lensDistortionInformationAvailable"
})
@XmlRootElement(name = "MD_ImageDescription")
@XmlSeeAlso(org.geotoolkit.internal.jaxb.gmi.MI_ImageDescription.class)
public class DefaultImageDescription extends DefaultCoverageDescription implements ImageDescription {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = -6168624828802439062L;

    /**
     * Illumination elevation measured in degrees clockwise from the target plane at
     * intersection of the optical line of sight with the Earths surface. For images from a
     * scanning device, refer to the centre pixel of the image.
     */
    private Double illuminationElevationAngle;

    /**
     * Illumination azimuth measured in degrees clockwise from true north at the time the
     * image is taken. For images from a scanning device, refer to the centre pixel of the image.
     */
    private Double illuminationAzimuthAngle;

    /**
     * Conditions affected the image.
     */
    private ImagingCondition imagingCondition;

    /**
     * Specifies the image quality.
     */
    private Identifier imageQualityCode;

    /**
     * Area of the dataset obscured by clouds, expressed as a percentage of the spatial extent.
     */
    private Double cloudCoverPercentage;

    /**
     * Image distributor's code that identifies the level of radiometric and geometric
     * processing that has been applied.
     */
    private Identifier processingLevelCode;

    /**
     * Count of the number the number of lossy compression cycles performed on the image.
     * {@code null} if the information is not provided.
     */
    private Integer compressionGenerationQuantity;

    /**
     * Indication of whether or not triangulation has been performed upon the image.
     * {@code null} if the information is not provided.
     */
    private Boolean triangulationIndicator;

    /**
     * Indication of whether or not the radiometric calibration information for generating the
     * radiometrically calibrated standard data product is available.
     */
    private Boolean radiometricCalibrationDataAvailable;

    /**
     * Indication of whether or not constants are available which allow for camera calibration
     * corrections.
     */
    private Boolean cameraCalibrationInformationAvailable;

    /**
     * Indication of whether or not Calibration Reseau information is available.
     */
    private Boolean filmDistortionInformationAvailable;

    /**
     * Indication of whether or not lens aberration correction information is available.
     */
    private Boolean lensDistortionInformationAvailable;

    /**
     * Constructs an initially empty image description.
     */
    public DefaultImageDescription() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy, or {@code null} if none.
     *
     * @since 2.4
     */
    public DefaultImageDescription(final ImageDescription source) {
        super(source);
    }

    /**
     * Returns a Geotk metadata implementation with the same values than the given arbitrary
     * implementation. If the given object is {@code null}, then this method returns {@code null}.
     * Otherwise if the given object is already a Geotk implementation, then the given object is
     * returned unchanged. Otherwise a new Geotk implementation is created and initialized to the
     * attribute values of the given object, using a <cite>shallow</cite> copy operation
     * (i.e. attributes are not cloned).
     *
     * @param  object The object to get as a Geotk implementation, or {@code null} if none.
     * @return A Geotk implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     *
     * @since 3.18
     */
    public static DefaultImageDescription castOrCopy(final ImageDescription object) {
        return (object == null) || (object instanceof DefaultImageDescription)
                ? (DefaultImageDescription) object : new DefaultImageDescription(object);
    }

    /**
     * Returns the illumination elevation measured in degrees clockwise from the target plane at
     * intersection of the optical line of sight with the Earth's surface. For images from a
     * scanning device, refer to the centre pixel of the image.
     * <p>
     * The horizon is at 0°, straight up has an elevation of 90°.
     */
    @Override
    @ValueRange(minimum=0, maximum=180)
    @XmlElement(name = "illuminationElevationAngle")
    public synchronized Double getIlluminationElevationAngle() {
        return illuminationElevationAngle;
    }

    /**
     * Sets the illumination elevation measured in degrees clockwise from the target plane at
     * intersection of the optical line of sight with the Earth's surface. For images from a
     * scanning device, refer to the centre pixel of the image.
     *
     * @param newValue The new illumination elevation angle.
     */
    public synchronized void setIlluminationElevationAngle(final Double newValue) {
        checkWritePermission();
        illuminationElevationAngle = newValue;
    }

    /**
     * Returns the illumination azimuth measured in degrees clockwise from true north at the time
     * the image is taken. For images from a scanning device, refer to the centre pixel of the
     * image.
     */
    @Override
    @ValueRange(minimum=0, maximum=360)
    @XmlElement(name = "illuminationAzimuthAngle")
    public synchronized Double getIlluminationAzimuthAngle() {
        return illuminationAzimuthAngle;
    }

    /**
     * Sets the illumination azimuth measured in degrees clockwise from true north at the time the
     * image is taken. For images from a scanning device, refer to the centre pixel of the image.
     *
     * @param newValue The new illumination azimuth angle.
     */
    public synchronized void setIlluminationAzimuthAngle(final Double newValue) {
        checkWritePermission();
        illuminationAzimuthAngle = newValue;
    }

    /**
     * Returns the conditions affected the image.
     */
    @Override
    @XmlElement(name = "imagingCondition")
    public synchronized ImagingCondition getImagingCondition() {
        return imagingCondition;
    }

    /**
     * Sets the conditions affected the image.
     *
     * @param newValue The new imaging condition.
     */
    public synchronized void setImagingCondition(final ImagingCondition newValue) {
        checkWritePermission();
        imagingCondition = newValue;
    }

    /**
     * Returns the identifier that specifies the image quality.
     */
    @Override
    @XmlElement(name = "imageQualityCode")
    public synchronized Identifier getImageQualityCode() {
        return imageQualityCode;
    }

    /**
     * Sets the identifier that specifies the image quality.
     *
     * @param newValue The new image quality code.
     */
    public synchronized void setImageQualityCode(final Identifier newValue) {
        checkWritePermission();
        imageQualityCode = newValue;
    }

    /**
     * Returns the area of the dataset obscured by clouds, expressed as a percentage of the spatial
     * extent.
     */
    @Override
    @ValueRange(minimum=0, maximum=100)
    @XmlElement(name = "cloudCoverPercentage")
    public synchronized Double getCloudCoverPercentage() {
        return cloudCoverPercentage;
    }

    /**
     * Sets the area of the dataset obscured by clouds, expressed as a percentage of the spatial
     * extent.
     *
     * @param newValue The new cloud cover percentage.
     */
    public synchronized void setCloudCoverPercentage(final Double newValue) {
        checkWritePermission();
        cloudCoverPercentage = newValue;
    }

    /**
     * Returns the image distributor's code that identifies the level of radiometric and geometric
     * processing that has been applied.
     */
    @Override
    @XmlElement(name = "processingLevelCode")
    public synchronized Identifier getProcessingLevelCode() {
        return processingLevelCode;
    }

    /**
     * Sets the image distributor's code that identifies the level of radiometric and geometric
     * processing that has been applied.
     *
     * @param newValue The new processing level code.
     */
    public synchronized void setProcessingLevelCode(final Identifier newValue) {
        checkWritePermission();
        processingLevelCode = newValue;
    }

    /**
     * Returns the count of the number of lossy compression cycles performed on the
     * image. Returns {@code null} if the information is not provided.
     */
    @Override
    @ValueRange(minimum=0)
    @XmlElement(name = "compressionGenerationQuantity")
    public synchronized Integer getCompressionGenerationQuantity() {
        return compressionGenerationQuantity;
    }

    /**
     * Sets the count of the number the number of lossy compression cycles performed on the image.
     *
     * @param newValue The new compression generation quantity.
     */
    public synchronized void setCompressionGenerationQuantity(final Integer newValue) {
        checkWritePermission();
        compressionGenerationQuantity = newValue;
    }

    /**
     * Returns the indication of whether or not triangulation has been performed upon the image.
     * Returns {@code null} if the information is not provided.
     */
    @Override
    @XmlElement(name = "triangulationIndicator")
    public synchronized Boolean getTriangulationIndicator() {
        return triangulationIndicator;
    }

    /**
     * Sets the indication of whether or not triangulation has been performed upon the image.
     *
     * @param newValue The new triangulation indicator.
     */
    public synchronized void setTriangulationIndicator(final Boolean newValue) {
        checkWritePermission();
        triangulationIndicator = newValue;
    }

    /**
     * Returns the indication of whether or not the radiometric calibration information for
     * generating the radiometrically calibrated standard data product is available.
     */
    @Override
    @XmlElement(name = "radiometricCalibrationDataAvailability")
    public synchronized Boolean isRadiometricCalibrationDataAvailable() {
        return radiometricCalibrationDataAvailable;
    }

    /**
     * Sets the indication of whether or not the radiometric calibration information for generating
     * the radiometrically calibrated standard data product is available.
     *
     * @param newValue {@code true} if radiometric calibration data are available.
     */
    public synchronized void setRadiometricCalibrationDataAvailable(final Boolean newValue) {
        checkWritePermission();
        radiometricCalibrationDataAvailable = newValue;
    }

    /**
     * Returns the indication of whether or not constants are available which allow for camera
     * calibration corrections.
     */
    @Override
    @XmlElement(name = "cameraCalibrationInformationAvailability")
    public synchronized Boolean isCameraCalibrationInformationAvailable() {
        return cameraCalibrationInformationAvailable;
    }

    /**
     * Sets the indication of whether or not constants are available which allow for camera
     * calibration corrections.
     *
     * @param newValue {@code true} if camera calibration information are available.
     */
    public synchronized void setCameraCalibrationInformationAvailable(final Boolean newValue) {
        checkWritePermission();
        cameraCalibrationInformationAvailable = newValue;
    }

    /**
     * Returns the indication of whether or not Calibration Reseau information is available.
     */
    @Override
    @XmlElement(name = "filmDistortionInformationAvailability")
    public synchronized Boolean isFilmDistortionInformationAvailable() {
        return filmDistortionInformationAvailable;
    }

    /**
     * Sets the indication of whether or not Calibration Reseau information is available.
     *
     * @param newValue {@code true} if film distortion information are available.
     */
    public synchronized void setFilmDistortionInformationAvailable(final Boolean newValue) {
        checkWritePermission();
        filmDistortionInformationAvailable = newValue;
    }

    /**
     * Returns the indication of whether or not lens aberration correction information is available.
     */
    @Override
    @XmlElement(name = "lensDistortionInformationAvailability")
    public synchronized Boolean isLensDistortionInformationAvailable() {
        return lensDistortionInformationAvailable;
    }

    /**
     * Sets the indication of whether or not lens aberration correction information is available.
     *
     * @param newValue {@code true} if lens distortion information are available.
     */
    public synchronized void setLensDistortionInformationAvailable(final Boolean newValue) {
        checkWritePermission();
        lensDistortionInformationAvailable = newValue;
    }
}
