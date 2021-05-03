# 🐍 The Snake Strikes Back 🐍

## Általános tudnivalók
A program az Alkalmazásfejlesztés I. tárgyra készült.
A projekt a Maven build automatizáló szoftvert használja. Függőségei között megtalálható a Java 11, JavaFX 11.
A projekt három modulból tevődik össze, amelyek **snake-core**, **snake-desktop**, **snake-web** névre hallgatnak.
A core modul tartalmazza a játék általt felhasznált adattípusokat, a desktop modul felelős az asztali alkalmazásért, míg a web modul a webes felületért.  
Az asztali változat legegyszerűbben a Maven Java FX plugin segítségével, a webes változat az Apache Tomcat szerver 9-es verziójával indítható a core modul Maven repository-ba installálása után.


## Asztali változat
A játék maga egy JavaFX alapú asztali alkalmazásban van megvalósítva.
A játékosoknak lehetősége van egyedüli, illetve kétszemélyes játékra is.
Az elért pontszámok egy központi adatbázisban tárolódnak egy-egy játék vége után.
Eme adatbázis megtekintésére és szerkesztésére is lehetőséget nyújt az alkalmazás.

### Egyjátékos mód
Egyjátékos mód során a játékos WASD gombok segítségével irányíthatja a kígyót.
A pályán megjelenő különböző ételek különböző színnel vannak ellátva, és különböző pontszámot érnek.
A játékos a megszerzett pontszámát és a nem régiben elfogyaszott ételét a képernyő tetején megjelenő címkékről olvashatja le.
A játékos akkor nyer, ha a kigyója kitölti az egész pályát.
Abban az esetben, ha a kígyó magába harap, a játéknak vége. Ez elkerülhető az E gomb megnyomásával aktiválható *éhség* képesség használatával.
Éhség esetén az önmagunkba harapás nem vet véget a játéknak, viszont a kígyó hossza csökken. Ez a képesség 3 másodpercig tart, és 10 másodpercenként használható.

### Kétjátékos mód
Kétjátékos mód során az első kígyót WASD, a második kígyót a nyíl gombok segítségével irányíthatjuk.
A játékmenet nagy részben megegyezik az egyjátékos móddal, a játék állapotáról itt is a képernyő felső részén megjelenő címkék leolvasásával tájékozódhatunk.
A második játékos az éhség képességét az M gomb megnyomásával aktiválhatja.
Ezen mód során a játékosok képesek lassítani vagy gyorsítani kígyóiakat, ha a mozgásirányuknak ellentétes, illetve megfelelő gombot lenntartják.
Továbbá egy új képesség, a *kannibál* is elérhető. Ez az első játékos számára a Q, a második játékos számára az N gomb megnyomásával aktiválható és 2 másodpercig tart, 120 másodperces várakozási idővel.
Ez az időtartam nullázható, ha a játékos megesz egy speciális, 0 pontot érő ételt. A képesség aktív ideje alatt a játékos átharaphatja az ellenfél kígyót, aminek következtében az lerövidül.
Inaktív kannibál képesség esetén egy ilyen harapás végzetes a kezdeményező kígyó számára, és az ellenfél automatikusan győz.

### Beállítási lehetősek
A beállítások menüben beállítható:
- A játékosok neve
- A játék alapsebessége
- A játékterület mérete (szélesség és magasság külön)
- A játékterületet körülvevő falak
- A kígyók fej illetve test színe
- Az egyes ételek színe (a speciális varázs étel kivételével)

### Ranglista
Az egyes játékok végén az elért eredmény egy ranglistába mentődik. Ez a ranglista megtekinthető az alkalmazásból.
Megnyitás után egy gomb segítségével válthatunk egyjátékos és kétjátékos eredmények között, törölhetjük azokat egyesével, kategória szerint, vagy az összeset egyben.
Továbbá lehetőség van az egyes bejegyzések szerkesztésére is.


## Webes változat
Webes felületen a ranglista megtekintésére és menedzselésére van lehetőség.
A kezdőoldalon a játékmód kiválasztása után látható a jelenlegi rangsor táblázatba szedve.
Itt a megfelelő gomb megnyomásával törölhető egy-egy bejegyzés. Módosításra az azonos nevű gomb megnyomása után, az űrlap kitöltésével és elküldésével van lehetőség.