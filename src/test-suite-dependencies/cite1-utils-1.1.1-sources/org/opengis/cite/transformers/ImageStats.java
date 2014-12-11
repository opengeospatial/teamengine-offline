package org.opengis.cite.transformers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.opengroup.ts.handlers.extensions.ogc.ResponseTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImageStats extends ResponseTransformer {

    private final static Logger LOGR = Logger.getLogger(ImageStats.class.getName());
    private String TransformedDocument;

    private ImageStats(Object response, InputStream parameters) {
        super(response, "");
        this.TransformedDocument = transform(response, parameters);
    }

    public ImageStats() {
        super();
    }

    public ImageStats(Object response, String parameters) {
        super(response, parameters);
        InputStream stream = new ByteArrayInputStream(parameters.getBytes());
        this.TransformedDocument = transform(response, stream);
    }

    private static String HexString(int num, int digits) {
        String s = Integer.toHexString(num);
        if (digits > s.length()) {
            return "00000000".substring(0, digits - s.length()) + s;
        }
        return s;
    }

    public String getTransformedDocument() {
        return this.TransformedDocument;
    }

    /**
     * Determines the specified properties of the given image. The property
     * nodes are updated accordingly.
     *
     * @param buffimage The image to process.
     * @param propNodes A NodeList that specifies the properties of interest.
     * @throws Exception
     */
    static void processBufferedImage(BufferedImage buffimage,
            NodeList propNodes) throws Exception {
        HashMap bandMap = new HashMap();
        for (int i = 0; i < propNodes.getLength(); i++) {
            Node node = propNodes.item(i);
            if (node.getNodeName().equals("subimage")) {
                Element e = (Element) node;
                int x = Integer.parseInt(e.getAttribute("x"));
                int y = Integer.parseInt(e.getAttribute("y"));
                int w = Integer.parseInt(e.getAttribute("width"));
                int h = Integer.parseInt(e.getAttribute("height"));
                processBufferedImage(buffimage.getSubimage(x, y, w, h),
                        e.getChildNodes());
            } else if (node.getNodeName().equals("checksum")) {
                CRC32 checksum = new CRC32();
                Raster raster = buffimage.getRaster();
                DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
                int numbanks = buffer.getNumBanks();
                for (int j = 0; j < numbanks; j++) {
                    checksum.update(buffer.getData(j));
                }
                Document doc = node.getOwnerDocument();
                node.appendChild(doc.createTextNode(Long.toString(checksum
                        .getValue())));
            } else if (node.getNodeName().equals("count")) {
                String band = ((Element) node).getAttribute("bands");
                String sample = ((Element) node).getAttribute("sample");
                if (sample.equals("all")) {
                    bandMap.put(band, null);
                } else {
                    HashMap sampleMap = (HashMap) bandMap.get(band);
                    if (sampleMap == null) {
                        if (!bandMap.containsKey(band)) {
                            sampleMap = new HashMap();
                            bandMap.put(band, sampleMap);
                        }
                    }
                    sampleMap.put(Integer.decode(sample), new Integer(0));
                }
            }
        }
        
        Iterator bandIt = bandMap.keySet().iterator();
        int y;
        while (bandIt.hasNext()) {
            String band_str = (String) bandIt.next();
            int[] band_indexes;
            if ((buffimage.getType() == BufferedImage.TYPE_BYTE_BINARY)
                    || (buffimage.getType() == BufferedImage.TYPE_BYTE_GRAY)) {
                band_indexes = new int[1];
                band_indexes[0] = 0;
            } else {
                band_indexes = new int[band_str.length()];
                for (int i = 0; i < band_str.length(); i++) {
                    if (band_str.charAt(i) == 'A') {
                        band_indexes[i] = 3;
                    }
                    if (band_str.charAt(i) == 'B') {
                        band_indexes[i] = 2;
                    }
                    if (band_str.charAt(i) == 'G') {
                        band_indexes[i] = 1;
                    }
                    if (band_str.charAt(i) == 'R') {
                        band_indexes[i] = 0;
                    }
                }
            }
            Raster raster = buffimage.getRaster();
            HashMap sampleMap = (HashMap) bandMap.get(band_str);
            boolean addall = sampleMap == null;
            if (sampleMap == null) {
                sampleMap = new HashMap();
                bandMap.put(band_str, sampleMap);
            }
            int minx = raster.getMinX();
            int maxx = minx + raster.getWidth();
            int miny = raster.getMinY();
            int maxy = miny + raster.getHeight();
            int[][] bands = new int[band_indexes.length][raster.getWidth()];

            y = miny;
            for (int i = 0; i < band_indexes.length; i++) {
                raster.getSamples(minx, y, maxx, 1, band_indexes[i], bands[i]);
            }
            for (int x = minx; x < maxx; x++) {
                int sample = 0;
                for (int i = 0; i < band_indexes.length; i++) {
                    sample |= bands[i][x] << (band_indexes.length - i - 1) * 8;
                }
                Integer sampleObj = new Integer(sample);
                boolean add = addall;
                if (!addall) {
                    add = sampleMap.containsKey(sampleObj);
                }
                if (add) {
                    Integer count = (Integer) sampleMap.get(sampleObj);
                    if (count == null) {
                        count = new Integer(0);
                    }
                    count = new Integer(count.intValue() + 1);
                    sampleMap.put(sampleObj, count);
                }
            }
            y++;
        }
        LOGR.log(Level.FINE, "Final bandMap:\n{0}", bandMap);
        Node node = propNodes.item(0);
        while (node != null) {
            if (node.getNodeName().equals("count")) {
                String band = ((Element) node).getAttribute("bands");
                String sample = ((Element) node).getAttribute("sample");
                HashMap sampleMap = (HashMap) bandMap.get(band);
                Document doc = node.getOwnerDocument();
                if (sample.equals("all")) {
                    Node parent = node.getParentNode();
                    Node prevSibling = node.getPreviousSibling();

                    Iterator sampleIt = sampleMap.keySet().iterator();
                    Element countnode = null;
                    int digits;
                    String prefix;
                    switch (buffimage.getType()) {
                        case 12:
                            digits = 1;
                            prefix = "";
                            break;
                        case 10:
                            digits = 2;
                            prefix = "0x";
                            break;
                        default:
                            prefix = "0x";
                            digits = band.length() * 2;
                    }
                    while (sampleIt.hasNext()) {
                        countnode = doc.createElement("count");
                        Integer sampleInt = (Integer) sampleIt.next();
                        Integer count = (Integer) sampleMap.get(sampleInt);
                        if (band.length() > 0) {
                            countnode.setAttribute("bands", band);
                        }
                        countnode.setAttribute(
                                "sample",
                                prefix
                                + HexString(sampleInt.intValue(),
                                digits));
                        Node textnode = doc.createTextNode(count.toString());
                        countnode.appendChild(textnode);
                        parent.insertBefore(countnode, node);
                        if (sampleIt.hasNext()) {
                            if (prevSibling.getNodeType() == 3) {
                                parent.insertBefore(
                                        prevSibling.cloneNode(false), node);
                            }
                        }
                    }
                    parent.removeChild(node);
                    node = countnode;
                } else {
                    Integer count = (Integer) sampleMap.get(Integer
                            .decode(sample));
                    if (count == null) {
                        count = new Integer(0);
                    }
                    Node textnode = doc.createTextNode(count.toString());
                    node.appendChild(textnode);
                }
            }
            node = node.getNextSibling();
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Produces summary statistics about an image. The requested information is
     * specified in an XML document (the document element is
     * {@code <image_stats>}).
     *
     * @param imgContent Image content retrieved using a URL connection (see
     * {@link URL#getContent() getContent}).
     * @param imgStats An InputStream for reading the parser directives.
     * @return A String containing the requested image information.
     */
    String transform(Object imgContent, InputStream imgStats) {
        if (System.getProperty("java.awt.headless") == null) {
            System.setProperty("java.awt.headless", "true");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageProducer producer;
            try {
                producer = (ImageProducer) imgContent;
            } catch (Exception e) {
                addLogMessage("Error: Not a recognized image format. " + e.getMessage());
                return null;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;
            try {
                doc = db.parse(imgStats);
            } catch (Exception e) {
                addLogMessage("Error: Could not parse image parameters.");
                addLogMessage(e.getClass().getName() + ": " + e.getMessage());
                return null;
            }
            Image image = Toolkit.getDefaultToolkit().createImage(producer);
            ImageTracker tracker = new ImageTracker(image);
            String type = tracker.getImageType();
            int height = image.getHeight(tracker);
            int width = image.getWidth(tracker);

            NodeList nodes = doc.getChildNodes();
            Node node = nodes.item(0);
            if (!node.getNodeName().equals("image_stats")) {
                addLogMessage("Error: Expecting root node to be named 'image_stats'.  Found '"
                        + node.getNodeName() + "'.");

                return null;
            }
            nodes = node.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                if (node.getNodeName().equals("type")) {
                    node.appendChild(doc.createTextNode(type));
                } else if (node.getNodeName().equals("height")) {
                    node.appendChild(doc.createTextNode(Integer
                            .toString(height)));
                } else if (node.getNodeName().equals("width")) {
                    node.appendChild(doc.createTextNode(Integer.toString(width)));
                } else if (node.getNodeName().equals("model")) {
                    String model = ((Element) node).getAttribute("value");
                    int imagetype;
                    if (model.equals("MONOCHROME")) {
                        imagetype = BufferedImage.TYPE_BYTE_BINARY; //12
                    } else if (model.equals("GRAY")) {
                        imagetype = BufferedImage.TYPE_BYTE_GRAY; //10
                    } else if (model.equals("RGB")) {
                        imagetype = BufferedImage.TYPE_3BYTE_BGR; //5
                    } else if (model.equals("ARGB")) {
                        imagetype = BufferedImage.TYPE_4BYTE_ABGR; //6
                    } else {
                        imagetype = BufferedImage.TYPE_CUSTOM; //0
                    }
                    BufferedImage buffImage = new BufferedImage(width, height,
                            imagetype);
                    Graphics2D g2 = buffImage.createGraphics();
                    // draw image into BufferedImage
                    g2.drawImage(image, 0, 0, tracker);
                    processBufferedImage(buffImage, node.getChildNodes());
                }
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(doc), new StreamResult(bos));
        } catch (Exception e) {
            addLogMessage(e.getClass().toString());
        }
        return bos.toString();
    }

    /**
     * Receives notifications about image information as it is constructed.
     */
    private class ImageTracker implements ImageObserver {

        private boolean done;
        private int flags;
        private Image image;
        private String type;

        public ImageTracker(Image image) {
            this(image, true);
        }

        public ImageTracker(Image image, boolean wait) {
            this.image = image;

            if (wait) {
                ImageTracker tracker = new ImageTracker(image, false);
                tracker.waitForImage();
                this.type = tracker.getImageType();
                this.done = true;
            } else {
                this.type = null;
                this.done = false;
            }
        }

        public String getImageType() {
            if (this.type == null) {
                while ((this.image.getWidth(this) == -1) && (!this.done)) {
                    ImageStats.sleep(50L);
                }
            }
            return this.type;
        }

        public synchronized boolean imageUpdate(Image img, int infoflags,
                int x, int y, int width, int height) {
            if (this.type == null) {
                try {
                    StringWriter sw = new StringWriter();
                    new Throwable("").printStackTrace(new PrintWriter(sw));
                    String stackTrace = sw.toString();
                    int i = stackTrace.indexOf("ImageDecoder.produceImage");
                    int j = stackTrace.lastIndexOf(".", i - 1);
                    this.type = stackTrace.substring(j + 1, i).toLowerCase();
                } catch (Exception e) {
                    this.type = "Could not determine";
                }
            }

            if ((infoflags & 0xC0) != 0) {
                this.done = true;
            } else {
                this.done = ((infoflags & 0x30) != 0);
            }
            return !this.done;
        }

        public boolean isDone() {
            return this.done;
        }

        public void waitForImage() {
            do {
                ImageStats.sleep(50L);

                if (this.image.getWidth(this) != -1) {
                    break;
                }
            } while (!this.done);

            while ((this.image.getHeight(this) == -1) && (!this.done)) {
                ImageStats.sleep(50L);
            }

            Toolkit tk = Toolkit.getDefaultToolkit();
            if (tk.prepareImage(this.image, -1, -1, this)) {
                this.done = true;
            }

            while (!this.done) {
                ImageStats.sleep(50L);
            }
        }
    }
}
