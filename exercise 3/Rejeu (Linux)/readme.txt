------------------------------
- Rejeu installation (Linux) -
------------------------------


Ivy
---

The easiest way is to install the Debian package ivy-c_3.11.10_amd64.deb provided 
(compiled on a 64bits machine, under Linux Ubuntu 14.04 but should work in a larger context)
Installation from a terminal :
	$ dpkg -i ivy-c_3.11.10_amd64.deb

Otherwise go to https://www.eei.cena.fr/products/ivy/
and get an adapted package or the source code to compile.


Rejeu
-----

Executable that does not require installation.
Make sure you have the right to run from a terminal pointing to the folder containing Rejeu.
	$ chmod +x rejeu

Launch from a terminal pointing to the folder containing Rejeu
	$ ./rejeu trafic.txt -s auto