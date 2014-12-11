<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet id="style" version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ivr="http://www.galdosinc.com/xml/instance/validation/report">
	<xsl:template match="ivr:InstanceValidationReport">
<html>
<head>
</head>
<body>
<h1>XML Instance Validation Report</h1>
<table border="1">
<tr>
<th>Line</th>
<th>Column</th>
<th>Message</th>
</tr>
<xsl:apply-templates select="ivr:Error" />
</table>
</body>
</html>
	</xsl:template>
	<xsl:template match="ivr:Error">
<tr>
<td><xsl:apply-templates select="@line" /></td>
<td><xsl:apply-templates select="@column" /></td>
<td><code><xsl:apply-templates /></code></td>
</tr>
	</xsl:template>
	<xsl:template match="xsl:stylesheet"/>

</xsl:stylesheet>
