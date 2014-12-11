package com.galdosinc.glib.gml.schema.xpsvi;

import com.galdosinc.glib.gml.schema.GmlObjectDefinition;
import com.galdosinc.glib.gml.schema.GmlPropertyDefinition;
import com.galdosinc.glib.gml.schema.GmlSchemaParser;
import com.galdosinc.glib.xml.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;

public class XpsviGmlObjectDefinition implements GmlObjectDefinition {
    private GmlSchemaParser model;
    private XSElementDeclaration element;

    public XpsviGmlObjectDefinition(GmlSchemaParser model,
            XSElementDeclaration element) {
        this.model = model;
        this.element = element;
    }

    public String getNamespaceUri() {
        return this.element.getNamespace();
    }

    public String getName() {
        return this.element.getName();
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isFeature() {
        return false;
    }

    public boolean isFeatureCollection() {
        return false;
    }

    public boolean isGeometry() {
        return false;
    }

    public boolean isGeometryCollection() {
        return false;
    }

    public Collection getProperties(XSElementDeclaration element) {
        List properties = new ArrayList();
        XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) element
                .getTypeDefinition();
        if (complexType == null) {
            complexType = element.getEnclosingCTDefinition();
        }
        setProperties(properties, complexType.getParticle());
        return properties;
    }

    private void setProperties(List properties, XSParticle particle) {
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration)) {
            properties
                    .add(new XPsviGmlPropertyDefinition(this.model, particle));
        } else if ((term instanceof XSModelGroup)) {
            XSModelGroup modelGroup = (XSModelGroup) term;
            XSObjectList particles = modelGroup.getParticles();
            for (int i = 0; i < particles.getLength(); i++)
                setProperties(properties, (XSParticle) particles.item(i));
        }
    }

    public GmlPropertyDefinition getProperty(String propertyNamespaceUri,
            String propertyLocalName) {
        XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) this.element
                .getTypeDefinition();
        if (complexType == null) {
            complexType = this.element.getEnclosingCTDefinition();
        }
        return getProperty(complexType.getParticle(), propertyNamespaceUri,
                propertyLocalName);
    }

    private GmlPropertyDefinition getProperty(XSParticle particle,
            String propertyNamespaceUri, String propertyLocalName) {
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration)) {
            if ((term.getNamespace().equals(propertyNamespaceUri))
                    && (term.getName().equals(propertyLocalName)))
                return new XPsviGmlPropertyDefinition(this.model, particle);
        } else if ((term instanceof XSModelGroup)) {
            XSModelGroup modelGroup = (XSModelGroup) term;
            XSObjectList particles = modelGroup.getParticles();
            for (int i = 0; i < particles.getLength(); i++) {
                GmlPropertyDefinition property = getProperty(
                        (XSParticle) particles.item(i), propertyNamespaceUri,
                        propertyLocalName);
                if (property != null) {
                    return property;
                }
            }
        }
        return null;
    }

    public Collection getPropertyNames() {
        List properties = new ArrayList();
        XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) this.element
                .getTypeDefinition();
        if (complexType == null) {
            complexType = this.element.getEnclosingCTDefinition();
        }
        setPropertyNames(properties, this.element, complexType.getParticle());
        return properties;
    }

    public Collection getMandatoryPropertyNames() {
        List properties = new ArrayList();
        XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) this.element
                .getTypeDefinition();
        if (complexType == null) {
            complexType = this.element.getEnclosingCTDefinition();
        }
        setMandatoryPropertyNames(properties, this.element,
                complexType.getParticle());
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

    private void setMandatoryPropertyNames(List properties,
            XSElementDeclaration element, XSParticle particle) {
        if (particle.getMinOccurs() > 0) {
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
                    setMandatoryPropertyNames(properties, element,
                            (XSParticle) particles.item(i));
            }
        }
    }

    public boolean isMandatoryProperty(String propertyNamespaceUri,
            String propertyLocalName) {
        Collection mandatoryProperties = getMandatoryPropertyNames();
        return mandatoryProperties.contains(new QName(propertyNamespaceUri,
                propertyLocalName));
    }

    protected GmlSchemaParser getSchemaParser() {
        return this.model;
    }

    protected XSElementDeclaration getElementDecl() {
        return this.element;
    }

    public boolean isTopoComplex() {
        return false;
    }

    public boolean isTopology() {
        return false;
    }
}