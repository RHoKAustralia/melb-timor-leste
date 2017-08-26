#!/usr/bin/python
#
# Escape strings in all strings.xml files in the app.
# Requires
# 	python 2.7 (might work with 3)
# 	lxml : sudo pip install lxml

from glob import glob
import lxml.etree as xml

files = glob('app/src/main/res/values*/strings.xml')

for file in files:
	root = xml.parse(file)
	for element in root.findall('string'):
		if "'" in element.text and not element.text.startswith('"'):
			element.text = u'"{}"'.format(element.text)
	root.write(file, encoding='utf-8')