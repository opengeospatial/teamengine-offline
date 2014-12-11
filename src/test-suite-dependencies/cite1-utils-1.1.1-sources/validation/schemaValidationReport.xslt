<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet id="style" version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:svr="http://www.galdosinc.com/xml/schema/validation/report">
	<xsl:template match="svr:SchemaValidationReport">
<html>
<head>
</head>
<body>
<h1>GML Schema Validation Report</h1>
<xsl:apply-templates />
</body>
</html>
  </xsl:template>
	<xsl:template match="svr:ElementReport">
<h2>Element: <xsl:apply-templates select="@namespaceUri" />#<xsl:apply-templates select="@name" /></h2>
<table border="1">
<tr>
<th>Message</th>
</tr>
<xsl:apply-templates select="svr:Error" />
</table>
	</xsl:template>
	<xsl:template match="svr:Error">
<tr>
<td><code><xsl:apply-templates /></code></td>
</tr>
	</xsl:template>
	<xsl:template match="xsl:stylesheet"/>

</xsl:stylesheet>
