package com.occamlab.te.index;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TemplateEntry extends IndexEntry {
    File templateFile = null;
    boolean usesContext;
    List<QName> params = null;

    TemplateEntry() {
        super();
    }

    TemplateEntry(Element template) {
        super(template);
        try {
            String file = template.getAttribute("file");
            if (file != null && file.length() > 0) {
                setTemplateFile(new File(new URI(template.getAttribute("file"))));
            }
            NodeList nl = template.getElementsByTagName("param");
            params = new ArrayList<QName>();
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                String prefix = el.getAttribute("prefix");
                String namespaceUri = el.getAttribute("namespace-uri");
                String localName = el.getAttribute("local-name");
                params.add(new QName(namespaceUri, localName, prefix));
            }
            setUsesContext(Boolean.parseBoolean(template
                    .getAttribute("uses-context")));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // public void persistAttributes(PrintWriter out) {
    // super.persistAttributes(out);
    // try {
    // out.print(" file=\"" + templateFile.toURI().toURL().toString() + "\""
    // + " uses-context=\"" + Boolean.toString(usesContext) + "\"");
    // } catch (MalformedURLException e) {
    // throw new RuntimeException(e);
    // }
    // }
    //
    // public void persistTags(PrintWriter out) {
    // super.persistTags(out);
    // for (QName qname : params) {
    // out.println("<param prefix=\"" + qname.getPrefix() + "\"" +
    // " namespace-uri=\"" + qname.getNamespaceURI() + "\"" +
    // " local-name=\"" + qname.getLocalPart() + "\"/>");
    // }
    // }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public List<QName> getParams() {
        return params;
    }

    public void setParams(List<QName> params) {
        this.params = params;
    }

    public boolean usesContext() {
        return usesContext;
    }

    public void setUsesContext(boolean usesContext) {
        this.usesContext = usesContext;
    }

    // static boolean freeExecutable() {
    // Set<String> keys = Globals.loadedExecutables.keySet();
    // synchronized(Globals.loadedExecutables) {
    // Iterator<String> it = keys.iterator();
    // if (it.hasNext()) {
    // Globals.loadedExecutables.remove(it.next());
    // return true;
    // }
    // }
    // return false;
    // }
    //
    // public XsltExecutable loadExecutable() throws SaxonApiException {
    // String key = getId();
    // XsltExecutable executable = Globals.loadedExecutables.get(key);
    // while (executable == null) {
    // try {
    // // System.out.println(template.getTemplateFile().getAbsolutePath());
    // Source source = new StreamSource(getTemplateFile());
    // executable = Globals.compiler.compile(source);
    // Globals.loadedExecutables.put(key, executable);
    // } catch (OutOfMemoryError e) {
    // boolean freed = freeExecutable();
    // if (!freed) {
    // throw e;
    // }
    // }
    // }
    //
    // Runtime rt = Runtime.getRuntime();
    // while (rt.totalMemory() - rt.freeMemory() > Globals.memThreshhold) {
    // boolean freed = freeExecutable();
    // if (!freed) {
    // break;
    // }
    // }
    //
    // return executable;
    // }
}
