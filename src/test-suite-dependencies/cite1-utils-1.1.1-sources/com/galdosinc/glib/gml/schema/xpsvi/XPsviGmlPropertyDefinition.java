package com.galdosinc.glib.gml.schema.xpsvi;

import com.galdosinc.glib.gml.schema.GmlPropertyDefinition;
import com.galdosinc.glib.gml.schema.GmlSchemaParser;
import java.util.LinkedList;
import java.util.List;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;

public class XPsviGmlPropertyDefinition implements GmlPropertyDefinition {
    private GmlSchemaParser model;
    private XSParticle particle;
    private XSElementDeclaration element;
    private List geometryTypeNames_;
    private List topologyTypeNames_;
    private XSTypeDefinition type;

    public XPsviGmlPropertyDefinition(GmlSchemaParser model, XSParticle particle) {
        this.model = model;
        this.particle = particle;
        XSTerm term = particle.getTerm();
        if ((term instanceof XSElementDeclaration))
            this.element = ((XSElementDeclaration) term);
        else {
            throw new IllegalArgumentException(
                    "A property can only be created from an element particle");
        }
        this.type = this.element.getTypeDefinition();
    }

    public String getNamespaceUri() {
        return this.element.getNamespace();
    }

    public String getName() {
        return this.element.getName();
    }

    public int getMinOccurs() {
        return this.particle.getMinOccurs();
    }

    public int getMaxOccurs() {
        return this.particle.getMaxOccurs();
    }

    public boolean isMandatory() {
        return getMinOccurs() > 0;
    }

    public boolean isGeometryProperty() {
        List geometryTypeNames = getGeometryTypeNames();
        return geometryTypeNames != null;
    }

    public boolean isTopologyProperty() {
        List topologyTypeNames = getTopologyTypeNames();
        return topologyTypeNames != null;
    }

    public boolean isSimpleValued() {
        if (this.type.getTypeCategory() == 14) {
            return true;
        }

        if (this.type.getTypeCategory() == 13) {
            int contentType = ((XSComplexTypeDefinition) this.type)
                    .getContentType();
            return contentType == 1;
        }
        return false;
    }

    public boolean canHaveRemoteValue() {
        if (this.type.getTypeCategory() == 14) {
            return true;
        }
        XSComplexTypeDecl complexType = (XSComplexTypeDecl) this.type;
        XSAttributeGroupDefinition attributeGroupDefinition = complexType
                .getAttrGrp();
        if (attributeGroupDefinition == null) {
            return false;
        }

        XSObjectList attrUses = attributeGroupDefinition.getAttributeUses();
        for (int ii = 0; ii < attrUses.getLength(); ii++) {
            XSAttributeUse attrUse = (XSAttributeUse) attrUses.item(ii);
            XSAttributeDeclaration attrDecl = attrUse.getAttrDeclaration();
            if ((attrDecl.getName().equals("href"))
                    && (attrDecl.getNamespace() != null)
                    && (attrDecl.getNamespace()
                            .equals("http://www.w3.org/1999/xlink"))) {
                return true;
            }
        }

        return false;
    }

    public boolean canHaveLocalValue() {
        return this.element.getEnclosingCTDefinition().getContentType() != 0;
    }

    public List getGeometryTypeNames() {
        if (this.geometryTypeNames_ != null) {
            return this.geometryTypeNames_;
        }
        this.geometryTypeNames_ = new LinkedList();
        if (this.model.getGeometryTypesFromGeometryProperty(this.element,
                this.geometryTypeNames_)) {
            return this.geometryTypeNames_;
        }
        return null;
    }

    public List getTopologyTypeNames() {
        if (this.topologyTypeNames_ != null) {
            return this.topologyTypeNames_;
        }
        this.topologyTypeNames_ = new LinkedList();
        if (this.model.getTopologyTypesFromTopologyProperty(this.element,
                this.topologyTypeNames_)) {
            return this.topologyTypeNames_;
        }
        return null;
    }

    public boolean isFeatureArrayProperty() {
        return this.model.isFeatureArrayProperty(this.element.getNamespace(),
                this.element.getName());
    }

    public boolean isFeatureValueProperty() {
        return (this.model.isFeatureMember(this.element.getNamespace(),
                this.element.getName()))
                || (this.model.isFeatureArrayProperty(
                        this.element.getNamespace(), this.element.getName()));
    }
}