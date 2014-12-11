package com.galdosinc.glib.gml.schema;

import com.galdosinc.glib.gml.schema.xpsvi.XPsviGmlFeatureDefinition;
import com.galdosinc.glib.gml.schema.xpsvi.XPsviGmlGeometryDefinition;
import com.galdosinc.glib.gml.schema.xpsvi.XPsviGmlTopologyDefinition;
import com.galdosinc.glib.xml.QName;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xni.grammars.XSGrammar;

public class GmlSchemaParser implements GmlConstants {
    private SchemaErrorHandler schemaErrorHandler;
    private GrammarErrorHandler grammarErrorHandler;
    private XSModel model;
    private List featureNames;
    private List geometryNames;
    private Map gmlElements = new HashMap();
    private Map gmlComplexTypes = new HashMap();
    private GmlVersion gmlVersion_;
    private GmlConstantUtils gmlConstantUtils_;

    public GmlSchemaParser() throws IOException {
        this.grammarErrorHandler = new GrammarErrorHandler();
    }

    public void parse(File file) throws IOException {
        this.model = ((XSGrammar) GrammarUtil.parseGrammar(file, null,
                this.grammarErrorHandler)).toXSModel();
        initialize();
    }

    public void parse(URL grammarUrl) throws IOException {
        this.model = ((XSGrammar) GrammarUtil.parseGrammar(grammarUrl, null,
                this.grammarErrorHandler)).toXSModel();
        initialize();
    }

    public void parse(String baseUri, String schemaText) throws IOException {
        this.model = ((XSGrammar) GrammarUtil.parseGrammar(baseUri, schemaText,
                null, this.grammarErrorHandler)).toXSModel();
        initialize();
    }

    public void parse(String baseUri, InputStream in) throws IOException {
        parse(baseUri, new InputStreamReader(in));
    }

    public void parse(String baseUri, Reader in) throws IOException {
        this.model = ((XSGrammar) GrammarUtil.parseGrammar(baseUri, in, null,
                this.grammarErrorHandler)).toXSModel();
        initialize();
    }

    private void initialize() {
        Class clazz = getClass();
        Field[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (((field.getModifiers() & 0x8) != 0)
                    && (field.getType() == QName.class)) {
                String fieldName = field.getName();
                try {
                    QName qName = (QName) field.get(null);

                    if (fieldName.endsWith("_TYPE_QNAME")) {
                        XSTypeDefinition typeDef = this.model
                                .getTypeDefinition(qName.getLocalName(),
                                        qName.getNamespaceUri());
                        if (typeDef != null)
                            this.gmlComplexTypes.put(qName, typeDef);
                    } else if (fieldName.endsWith("_ELEMENT_QNAME")) {
                        XSElementDeclaration elemDecl = this.model
                                .getElementDeclaration(qName.getLocalName(),
                                        qName.getNamespaceUri());
                        if (elemDecl != null)
                            this.gmlElements.put(qName, elemDecl);
                    }
                } catch (IllegalAccessException localIllegalAccessException) {
                }
            }
        }
        boolean isGML3 = this.gmlElements
                .containsKey(GmlConstants.ABS_GML_ELEMENT_QNAME);
        this.gmlVersion_ = (isGML3 ? GmlVersion.GML_3 : GmlVersion.GML_2);
        this.gmlConstantUtils_ = new GmlConstantUtils(this.gmlVersion_);
    }

    public GmlVersion getGmlVersion() {
        return this.gmlVersion_;
    }

    public GmlFeatureDefinition getFeature(String namespaceUri, String name) {
        XSElementDeclaration element = this.model.getElementDeclaration(name,
                namespaceUri);
        return element == null ? null : new XPsviGmlFeatureDefinition(this,
                element);
    }

    public GmlGeometryDefinition getGeometry(String namespaceUri, String name) {
        XSElementDeclaration element = this.model.getElementDeclaration(name,
                namespaceUri);
        return element == null ? null : new XPsviGmlGeometryDefinition(this,
                element);
    }

    public GmlTopologyDefinition getTopology(String namespaceUri, String name) {
        XSElementDeclaration element = this.model.getElementDeclaration(name,
                namespaceUri);
        return element == null ? null : new XPsviGmlTopologyDefinition(this,
                element);
    }

    public int getCorrespondingGmlGeometryTypeCode(String namespaceUri,
            String name) {
        GmlGeometryDefinition geomDef = getGeometry(namespaceUri, name);
        if (geomDef == null) {
            return -1;
        }
        return geomDef.getCorrespondingGmlGeometryTypeCode();
    }

    public XSElementDeclaration getGmlElement(QName qName) {
        return (XSElementDeclaration) this.gmlElements.get(qName);
    }

    public XSComplexTypeDefinition getGmlType(QName qName) {
        return (XSComplexTypeDefinition) this.gmlComplexTypes.get(qName);
    }

    public Collection getFeatureNames() {
        if (this.featureNames == null) {
            this.featureNames = new ArrayList();
            XSNamedMap globalElements = this.model.getComponents((short) 2);
            for (int i = 0; i < globalElements.getLength(); i++) {
                XSElementDeclaration element = (XSElementDeclaration) globalElements
                        .item(i);
                String namespaceUri = element.getNamespace();
                String localName = element.getName();
                if ((isFeature(element)) && (!element.getAbstract())) {
                    this.featureNames.add(new QName(namespaceUri, localName));
                }
            }
        }
        return this.featureNames;
    }

    public Collection getGeometryNames() {
        if (this.geometryNames == null) {
            this.geometryNames = new ArrayList();
            XSNamedMap globalElements = this.model.getComponents((short) 2);
            for (int i = 0; i < globalElements.getLength(); i++) {
                XSElementDeclaration element = (XSElementDeclaration) globalElements
                        .item(i);
                String namespaceUri = element.getNamespace();
                String localName = element.getName();
                if ((isGeometry(element)) && (!element.getAbstract())) {
                    this.geometryNames.add(new QName(namespaceUri, localName));
                }
            }
        }
        return this.geometryNames;
    }

    public QName getElementType(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        XSTypeDefinition type = element.getTypeDefinition();
        String typeNamespaceUri = type.getNamespace();
        String typeName = type.getName();
        return new QName(typeNamespaceUri, typeName);
    }

    public XSElementDeclaration getElement(String namespaceUri, String localName) {
        return this.model.getElementDeclaration(localName, namespaceUri);
    }

    public QName getGeometryType(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return getGeometryType(element);
    }

    public QName getGeometryType(XSElementDeclaration element) {
        XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) element
                .getTypeDefinition();
        if (complexType == null) {
            complexType = element.getEnclosingCTDefinition();
        }
        return getGeometryType(complexType);
    }

    public QName getGeometryType(XSComplexTypeDefinition type) {
        String namespaceUri = type.getNamespace();
        String localName = type.getName();
        QName typeName = new QName(namespaceUri, localName);
        if (this.gmlConstantUtils_.isGeometryType(typeName)) {
            return typeName;
        }
        XSComplexTypeDefinition parent = (XSComplexTypeDefinition) type
                .getBaseType();
        if ((parent == type) || (parent == null)) {
            return null;
        }
        return getGeometryType(type);
    }

    public Collection getMandatoryProperties(String namespaceUri, String name) {
        return getFeature(namespaceUri, name).getMandatoryPropertyNames();
    }

    public List getGeometryTypeFromGeometryProperty(String featureNamespaceUri,
            String featureName, String propertyNamespaceUri, String propertyName) {
        GmlFeatureDefinition feature = getFeature(featureNamespaceUri,
                featureName);
        GmlPropertyDefinition property = feature.getProperty(
                propertyNamespaceUri, propertyName);
        return property.getGeometryTypeNames();
    }

    public List getGeometryTypesFromGeometryProperty(String namespaceUri,
            String localName) {
        List result = new LinkedList();
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return getGeometryTypesFromGeometryProperty(element, result) ? result
                : null;
    }

    public boolean getGeometryTypesFromGeometryProperty(
            XSElementDeclaration element, List result) {
        XSTypeDefinition type = element.getTypeDefinition();

        if ((type instanceof XSComplexTypeDefinition)) {
            XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) type;
            XSParticle particle = complexType.getParticle();

            if (particle == null)
                return false;
            return getGeometryTypesFromGeometryProperty(particle, result);
        }
        return false;
    }

    public boolean getGeometryTypesFromGeometryProperty(XSParticle particle,
            List result) {
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration)) {
            XSElementDeclaration geometryElement = (XSElementDeclaration) term;
            if (isGeometry(geometryElement)) {
                QName geometryElemQName = new QName(
                        geometryElement.getNamespace(),
                        geometryElement.getName());
                result.add(geometryElemQName);
                return true;
            }
        } else if ((term instanceof XSModelGroup)) {
            XSModelGroup modelGroup = (XSModelGroup) term;
            XSObjectList particles = modelGroup.getParticles();
            int particleCount = particles.getLength();
            boolean found = false;
            for (int ii = 0; ii < particleCount; ii++) {
                XSParticle subParticle = (XSParticle) particles.item(ii);
                found |= getGeometryTypesFromGeometryProperty(subParticle,
                        result);
            }
            return found;
        }

        return false;
    }

    public Collection getSimpleTypeProperties(String namespaceUri,
            String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return getSimpleTypeProperties(element);
    }

    public Collection getSimpleTypeProperties(XSElementDeclaration element) {
        List properties = new ArrayList();
        if (isFeature(element)) {
            XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) element
                    .getTypeDefinition();
            if (complexType == null) {
                complexType = element.getEnclosingCTDefinition();
            }
            setSimpleTypeProperties(properties, element,
                    complexType.getParticle());
        }
        return properties;
    }

    private void setSimpleTypeProperties(List properties,
            XSElementDeclaration element, XSParticle particle) {
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration)) {
            XSElementDeclaration propertyElement = (XSElementDeclaration) term;

            if ((propertyElement.getTypeDefinition() instanceof XSSimpleTypeDefinition)) {
                String namespaceUri = propertyElement.getNamespace();
                String localName = propertyElement.getName();
                properties.add(new QName(namespaceUri, localName));
            }
        } else if ((term instanceof XSModelGroup)) {
            XSModelGroup modelGroup = (XSModelGroup) term;
            XSObjectList particles = modelGroup.getParticles();
            for (int i = 0; i < particles.getLength(); i++)
                setSimpleTypeProperties(properties, element,
                        (XSParticle) particles.item(i));
        }
    }

    public Collection getPropertyNames(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return getPropertyNames(element);
    }

    public Collection getPropertyNames(XSElementDeclaration element) {
        List properties = new ArrayList();
        if (isFeature(element)) {
            XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) element
                    .getTypeDefinition();
            if (complexType == null) {
                complexType = element.getEnclosingCTDefinition();
            }
            setPropertyNames(properties, element, complexType.getParticle());
        }
        return properties;
    }

    private void setPropertyNames(List properties,
            XSElementDeclaration element, XSParticle particle) {
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration)) {
            XSElementDeclaration propertyElement = (XSElementDeclaration) term;
            String namespaceUri = propertyElement.getNamespace();
            String localName = propertyElement.getName();
            properties.add(new QName(namespaceUri, localName));
        } else if ((term instanceof XSModelGroup)) {
            XSModelGroup modelGroup = (XSModelGroup) term;
            XSObjectList particles = modelGroup.getParticles();
            for (int i = 0; i < particles.getLength(); i++)
                setPropertyNames(properties, element,
                        (XSParticle) particles.item(i));
        }
    }

    public boolean isFeature(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isFeature(element);
    }

    public boolean isFeature(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        if (element == null)
            return false;
        return isFeature(element);
    }

    public boolean isFeature(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.ABSTRACT_FEATURE_TYPE_QNAME));
    }

    public boolean isFeatureCollection(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isFeatureCollection(element);
    }

    public boolean isFeatureCollection(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isFeatureCollection(element);
    }

    public boolean isFeatureCollection(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.ABS_FEATURE_COLLECTION_TYPE_QNAME));
    }

    public boolean isFeatureArrayProperty(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isFeatureArrayProperty(element);
    }

    public boolean isFeatureArrayProperty(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isFeatureArrayProperty(element);
    }

    public boolean isFeatureArrayProperty(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.FEATURE_ARRAY_PROPERTY_TYPE_QNAME));
    }

    public boolean isFeatureMember(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isFeatureMember(element);
    }

    public boolean isFeatureMember(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isFeatureMember(element);
    }

    public boolean isFeatureMember(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return (isExtensionOf(type,
                getGmlType(GmlConstants.FEATURE_ASSOCIATION_TYPE_QNAME)))
                || (isExtensionOf(type,
                        getGmlType(GmlConstants.FEATURE_PROPERTY_TYPE_QNAME)));
    }

    public boolean isGeometry(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isGeometry(element);
    }

    public boolean isGeometry(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isGeometry(element);
    }

    public boolean isGeometry(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.ABS_GEOMETRY_TYPE_QNAME));
    }

    public boolean isGeometryCollection(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isGeometryCollection(element);
    }

    public boolean isGeometryCollection(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isGeometryCollection(element);
    }

    public boolean isGeometryCollection(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        if (isExtensionOf(type,
                getGmlType(GmlConstants.GEOMETRY_COLLECTION_TYPE_QNAME))) {
            return true;
        }
        return false;
    }

    public boolean getTopologyTypesFromTopologyProperty(
            XSElementDeclaration element, List result) {
        XSTypeDefinition type = element.getTypeDefinition();

        if ((type instanceof XSComplexTypeDefinition)) {
            XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) type;
            XSParticle particle = complexType.getParticle();

            if (particle == null)
                return false;
            return getTopologyTypesFromTopologyProperty(particle, result);
        }
        return false;
    }

    public boolean getTopologyTypesFromTopologyProperty(XSParticle particle,
            List result) {
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration)) {
            XSElementDeclaration topologyElement = (XSElementDeclaration) term;
            if (isTopology(topologyElement)) {
                QName topologyElemQName = new QName(
                        topologyElement.getNamespace(),
                        topologyElement.getName());
                result.add(topologyElemQName);
                return true;
            }
        } else if ((term instanceof XSModelGroup)) {
            XSModelGroup modelGroup = (XSModelGroup) term;
            XSObjectList particles = modelGroup.getParticles();
            int particleCount = particles.getLength();
            boolean found = false;
            for (int ii = 0; ii < particleCount; ii++) {
                XSParticle subParticle = (XSParticle) particles.item(ii);
                found |= getTopologyTypesFromTopologyProperty(subParticle,
                        result);
            }
            return found;
        }
        return false;
    }

    public boolean isTopology(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isTopology(element);
    }

    public boolean isTopology(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isTopology(element);
    }

    public boolean isTopology(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        boolean isTopology = (isExtensionOf(type,
                getGmlType(GmlConstants.ABS_TOPOLOGY_TYPE_QNAME)))
                || (isExtensionOf(type,
                        getGmlType(GmlConstants.TOPO_POINT_TYPE_QNAME)))
                || (isExtensionOf(type,
                        getGmlType(GmlConstants.TOPO_CURVE_TYPE_QNAME)))
                || (isExtensionOf(type,
                        getGmlType(GmlConstants.TOPO_SURFACE_TYPE_QNAME)))
                || (isExtensionOf(type,
                        getGmlType(GmlConstants.TOPO_VOLUME_TYPE_QNAME)));
        return isTopology;
    }

    public boolean isTopoComplex(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isTopoComplex(element);
    }

    public boolean isTopoComplex(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isTopoComplex(element);
    }

    public boolean isTopoComplex(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.TOPO_COMPLEX_TYPE_QNAME));
    }

    public boolean isTopoComplexMember(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isTopoComplexMember(element);
    }

    public boolean isTopoComplexMember(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isTopoComplexMember(element);
    }

    public boolean isTopoComplexMember(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.TOPO_COMPLEX_MEMBER_TYPE_QNAME));
    }

    public boolean isTopoPrimitiveMember(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isTopoPrimitiveMember(element);
    }

    public boolean isTopoPrimitiveMember(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isTopoPrimitiveMember(element);
    }

    public boolean isTopoPrimitiveMember(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.TOPO_PRIMITIVE_MEMBER_TYPE_QNAME));
    }

    public boolean isTopoPrimitiveArrayAssociation(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isTopoPrimitiveArrayAssociation(element);
    }

    public boolean isTopoPrimitiveArrayAssociation(String namespaceUri,
            String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isTopoPrimitiveArrayAssociation(element);
    }

    public boolean isTopoPrimitiveArrayAssociation(XSElementDeclaration element) {
        XSTypeDefinition type = element.getTypeDefinition();
        return isExtensionOf(
                type,
                getGmlType(GmlConstants.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE_QNAME));
    }

    public boolean isGmlObject(String namespaceUri, String localName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                localName, namespaceUri);
        if (element == null) {
            return false;
        }
        return isGmlObject(element);
    }

    public boolean isGmlObject(QName qName) {
        XSElementDeclaration element = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        if (element == null) {
            return false;
        }
        return isGmlObject(element);
    }

    public boolean isGmlObject(XSElementDeclaration objectElement) {
        XSTypeDefinition type = objectElement.getTypeDefinition();
        return isExtensionOf(type,
                getGmlType(GmlConstants.ABSTRACT_GML_TYPE_QNAME));
    }

    public boolean isSubstitutableFeature(String namespaceUri, String localName) {
        XSElementDeclaration elementDef = this.model.getElementDeclaration(
                localName, namespaceUri);
        if (isSubstitutable(elementDef,
                getGmlElement(GmlConstants.ABS_FEATURE_ELEMENT_QNAME))) {
            return true;
        }
        return false;
    }

    public boolean isSubstitutableFeatureCollection(String namespaceUri,
            String localName) {
        XSElementDeclaration elementDef = this.model.getElementDeclaration(
                localName, namespaceUri);
        if (isSubstitutable(
                elementDef,
                getGmlElement(GmlConstants.ABS_FEATURE_COLLECTION_ELEMENT_QNAME))) {
            return true;
        }
        return false;
    }

    public boolean isSubstitutable(QName qName, QName substQName)
            throws GMLSchemaParserException {
        XSElementDeclaration elementDef = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        if (elementDef == null) {
            throw new GMLSchemaParserException("Name '" + qName.getLocalName()
                    + "' is not globaly defined in the namespace '"
                    + qName.getNamespaceUri() + "'");
        }
        if (isSubstitutable(elementDef, getGmlElement(substQName))) {
            return true;
        }
        return false;
    }

    private boolean isSubstitutable(XSElementDeclaration type,
            XSElementDeclaration baseType) {
        if (type.equals(baseType)) {
            return true;
        }
        XSElementDeclaration parent = type.getSubstitutionGroupAffiliation();
        if ((parent == type) || (parent == null)) {
            return false;
        }
        return isSubstitutable(parent, baseType);
    }

    public boolean validateFeature(String namespaceUri, String localName) {
        XSElementDeclaration featureElement = this.model.getElementDeclaration(
                localName, namespaceUri);
        if (featureElement == null) {
            this.schemaErrorHandler.error(namespaceUri, localName,
                    "The specified feature does not exist in the schema");
            return false;
        }
        return validateFeature(featureElement);
    }

    public boolean validateFeature(XSElementDeclaration featureElement) {
        boolean featureValid = true;
        if (!isFeature(featureElement)) {
            reportError(featureElement.getNamespace(),
                    featureElement.getName(),
                    "A feature's type definition must extend from gml:AbstractFeatureType.");
            featureValid = false;
        }
        XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) featureElement
                .getTypeDefinition();
        if (complexType == null) {
            complexType = featureElement.getEnclosingCTDefinition();
        }
        if (!containsOnlyProperties(featureElement, complexType.getParticle())) {
            featureValid = false;
        }
        return featureValid;
    }

    public boolean validateFeatures(List features) {
        boolean allFeaturesValid = true;
        for (Iterator typeNames = features.iterator(); typeNames.hasNext();) {
            QName featureTypeName = (QName) typeNames.next();
            String namespaceUri = featureTypeName.getNamespaceUri();
            String localName = featureTypeName.getLocalName();
            if (!validateFeature(namespaceUri, localName)) {
                allFeaturesValid = false;
            }
        }
        return allFeaturesValid;
    }

    public boolean validateFeatures() {
        boolean allFeaturesValid = true;
        XSNamedMap globalElements = this.model.getComponents((short) 2);
        for (int i = 0; i < globalElements.getLength(); i++) {
            XSElementDeclaration element = (XSElementDeclaration) globalElements
                    .item(i);
            if ((isFeature(element)) && (!validateFeature(element))) {
                allFeaturesValid = false;
            }
        }
        return allFeaturesValid;
    }

    private boolean containsOnlyProperties(XSElementDeclaration featureElement,
            XSParticle particle) {
        boolean containsOnlyProperties = true;
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration)) {
            if (!validateProperty(featureElement, (XSElementDeclaration) term))
                containsOnlyProperties = false;
        } else if ((term instanceof XSModelGroup)) {
            XSModelGroup modelGroup = (XSModelGroup) term;
            XSObjectList particles = modelGroup.getParticles();
            for (int i = 0; i < particles.getLength(); i++) {
                if (!containsOnlyProperties(featureElement,
                        (XSParticle) particles.item(i))) {
                    containsOnlyProperties = false;
                }
            }
        }
        return containsOnlyProperties;
    }

    public boolean isProperty(QName qName) {
        XSElementDeclaration elementDef = this.model.getElementDeclaration(
                qName.getLocalName(), qName.getNamespaceUri());
        return isProperty(elementDef);
    }

    public boolean isProperty(String namespaceUri, String localName) {
        XSElementDeclaration elementDef = this.model.getElementDeclaration(
                localName, namespaceUri);
        return isProperty(elementDef);
    }

    private boolean isProperty(XSElementDeclaration element) {
        if ((isFeature(element)) || (isGeometry(element))) {
            return false;
        }
        return true;
    }

    private boolean validateProperty(XSElementDeclaration featureElement,
            XSElementDeclaration element) {
        boolean validProperty = true;
        if (isFeature(element)) {
            reportError(featureElement.getNamespace(),
                    featureElement.getName(),
                    "A feature cannot have a feature as a direct child: "
                            + element.getNamespace() + "#" + element.getName()
                            + ".");
            validProperty = false;
        }
        if (isGeometry(element)) {
            reportError(featureElement.getNamespace(),
                    featureElement.getName(),
                    "A feature cannot have a geometry as a direct child: "
                            + element.getNamespace() + "#" + element.getName()
                            + ".");
            validProperty = false;
        }
        return validProperty;
    }

    public static boolean isExtensionOf(XSTypeDefinition type,
            XSTypeDefinition baseType) {
        if (type.equals(baseType)) {
            return true;
        }
        XSTypeDefinition parent = type.getBaseType();
        if (parent == null)
            return false;
        if (parent == type) {
            return false;
        }
        return isExtensionOf(parent, baseType);
    }

    private void reportError(String message) {
        if (this.schemaErrorHandler != null)
            this.schemaErrorHandler.error(message);
    }

    private void reportError(String namespaceUri, String localName,
            String message) {
        if (this.schemaErrorHandler != null)
            this.schemaErrorHandler.error(namespaceUri, localName, message);
    }

    public SchemaErrorHandler getSchemaErrorHandler() {
        return this.schemaErrorHandler;
    }

    public void setSchemaErrorHandler(SchemaErrorHandler schemaErrorHandler) {
        this.schemaErrorHandler = schemaErrorHandler;
        this.grammarErrorHandler.setSchemaErrorHandler(schemaErrorHandler);
    }
}