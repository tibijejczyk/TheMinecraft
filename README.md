TheMinecraft
============

Projekt serwerowni TheMinecraft.

Jakich narzedzi uzywac?
============
Uzywaj tylko Git GUI z paczki http://code.google.com/p/gitextensions/
eGIT oraz Github sa zbugowane i nie nadaja sie dla tak duzej liczby osob (Jesli jeden plik uzywa wiecej niz 1 osoba GitHub i eGit glupieja calkowicie, dlatego zalecana jest konsola).


Jak rozwiazac Merge Conflict
============
1. W Git GUI otwieramy nasze repo (src).
3. Na dole w Commit Message wpisz swoja wiadomosc.
4. State changed -> Commit -> Push -> Wprowadz swoje pasy. Powinienes dostac cos takiego:

Pushing to https://github.com/dex3r/TheMinecraft.git
To https://github.com/dex3r/TheMinecraft.git
 ! [rejected]        master -> master (non-fast-forward)
error: failed to push some refs to 'https://github.com/dex3r/TheMinecraft.git'
hint: Updates were rejected because the tip of your current branch is behind
hint: its remote counterpart. Merge the remote changes (e.g. 'git pull')
hint: before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.

5. Wejdz w Merge -> Local Merge... -> Merge.
5.a. Jesli dostaniesz blad krytyczny, musisz najpierw pobrac branche, klikajac Remote -> Fetch from... -> origin.
6. Powinienes dostac cos takiego:

Auto-merging minecraft/dex3r/main/factions/Faction.java
CONFLICT (content): Merge conflict in minecraft/dex3r/main/factions/Faction.java
Automatic merge failed; fix conflicts and then commit the result.

7. Factions.java to nazwa pliku, w ktorym wystapil merge conflict. 
8. Wejdz w Tools -> Add... -> Jako nazwe wpisz np. "Merge conflict res", a jako komende "git mergetool -y --no-prompt -t kdiff3". Zaznasz "Add globally"
9. Jesli masz juz to narzedzie, uruchom je.
10. Teraz wyrozniamy 2 przypadki: w pierwszym konflikt zostanie rozwiazany automatycznie, w drugim, otworzy ci sie narzedzie do jego usuwania. W drugim przypadku usun konflikt recznie i zapisz.
11. Commit -> Push -> Push.


Dev team:
Oskar13 aka tibijejczyk;
DeX3r;
Raxon;
Omlet;
Janto;
