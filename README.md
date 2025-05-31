# Brain Boost Alarm

## Opis domene

### Ciljana platforma aplikacije

Ciljana platforma za koju se aplikacija razvija je Android operacijski sustav. SDK (Software development kit) koji se koristi pri razvoju je API 24 ("Nougat"; Android 7.0) što znači da će se aplikacija moći preuzeti, instalirati i izvoditi na svim verzijama androida koje su novije od 7.0 verzije (uključujući 7.0). Prema trenutnim procjenama aplikacija će se moći izvoditi na otprilike 96% svih Android uređaja.

### Problem koji aplikacija rješava

Problem koji aplikacija rješava je inercija sna (eng. sleep interia). Inercija sna stanje je u kojem se osoba osjeća pospano u trenutcima kada je probuđena. Američki Centar za kontrolu i prevenciju bolesti (CDC) to definira kao privremenu dezorijentaciju i pad raspoloženja nakon buđenja iz sna. U tom stanju ljudi su skloni vratiti se spavanju i isključiti alarm koji ih je probudio. Aplikacija će od korisnika zahtjevati da prije nego što isključi alarm riješi određeni izazov i tako će minimizirati utjecaj inercije sna.

### Opis aplikacije

Brain Boost Alarm zamjena je za aplikaciju alarma koja dolazi kao predinstalirana za svaki pametni uređaj. Korisnik u aplikaciji postavavlja vrijeme kada želi da alarm zazvoni. Kada se alarm oglasi korisnik ne može na uobičajen način jednostavno isključiti zvuk alarma već mora riješiti kratki izazov kako bi isključio zvuk alarma. Izazov koji korisnik mora riješiti je slučajno generiran, a u početnoj verziji planira se razviti 3-4 izazova koji će se generirati korisniku i koje mora riješiti kako bi isključio zvuk alarma.

Prvi je izazov kratki matematički test, a uključuje 4-5 jednostavna matematička zadatka koje korisnik mora riješiti kako bi isključio alarm. Brojevi u zadacima su svaki puta drugačiji odnosno nasumično su generirani. Prva 2-3 zadatka jednostavnija i uključuju jednostavne računske operacije kao što su zbrajanje i oduzimanje, a druga dva su složenija i uključuju množenje, dijeljenje, računanje površine ili opsega geometrijskog lika ili izračunavanje nepoznanice u nekoj jednostavnijoj jednadžbi.

Drugi izazov koji se može generirati korisniku je kratka igrica u kojoj se generiraju ikonice kave i bombe koji se kreću od vrha zaslona prema dnu - kao da padaju s neba, takozvana catcher igrica. Korisnik mora uhvatiti sve ikonice kave koje padaju s neba kako bi uspješno riješio izazov, a pri tome ne smije uhvatiti ni jednu bombu. U slučaju da uhvati bombu korisnik mora cijeli izazov riješiti iznova kako bi isključio alarm. Korisnik ikonice hvata pritiskom prsta na padajuću ikonu.

Posljednji izazov koji će se potencijalno generirati korisniku je 2d top-down igrica koja simulira popularnu zmiju. Korisnik će u igrici morati sakupiti 10 ikonica kave kako bi uspješno riješio izazov i isključio alarm.

### Kategorizacija aplikacije na Trgovini Play

Trenutno na Trgovina Play (eng. Google Play Store, Play Store) postoji preko 30 različitih kategorija aplikacija. Aplikacija koja se razvija po opisu najviše odgovara kategorijama 'Životni stil' i 'Produktivnost'.
U kategoriji 'Životni stil' aplikacije su namijenjene pružanju podrške i automatizaciji dnevnih aktivnosti korisnika - aplikacije za dostavu hrane, za upravljanje IoT uređajima u kućanstvu i slično. U kategoriji 'Produktivnost' prevladavaju aplikacije i alati namijenjeni pružanju podrške i automatizaciji poslovnih aktivnosti korisnika - aplikacije za obradu teksta, za skeniranje dokumenata, za antivirusno skeniranje uređaja, kalendari, rasporedi i slično.
Može se reći da će aplikacija utjecati i na poslovne i na privatne aktivnosti korisnika jer će služiti kao alat koji će ga u zadano vrijeme probuditi i pri tome će korisnika prisiliti da se potpuno probudi i započne sa svojim poslovnim ili privatnim aktivnostima.

## Specifikacija projekta

| Oznaka | Naziv                                         | Kratki opis                                                                                                                                                                                                                                                                                                                                                                                    | Zaduženi član                  |
| ------ | --------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------ |
| F01    | Prikaz svih alarma u listi                    | Na glavnom sučelju aplikacije prikazuju se svi kreirani alarmi od strane korisnika u obliku liste. Ukoliko nije kreiran ni jedan alarm na srediti se nalazi slika emotikona koji spava te tekst sa obavijesti korisniku kako trenutno nema kreiranih alarma.                                                                                                                                   | 
| F02    | Kreiranje novog alarma                        | Na glavnom sučelju nalazi se ikona plusa. Klikom na ikonu plusa korisnik kreira novi alarm prilikom ćega odabire dane te vrijeme kada će se alarm oglasiti. Nakon što korisnik kreira alarm aplikacija će korisnika obavijestiti korisnika da je alarm kreiran i kada će se oglasiti.                                                                                                          | 
| F03    | Upravljanje alarmima iz liste                 | Korisnik ima mogućnost klika na prikazane alarme nakon ćega će se otvoriti dijalog sa mogućnošću izmjene dana i vremena kada će se alarm oglasiti. Korisnik će imati mogućnost da odustane od promjena ili da spremi promjene te mogućnost da obriše alarm.                                                                                                                                    | 
| F04    | Oglašavanje alarma                            | Kreirani alarm od strane korisnika oglasit će se u trenutku koji je korisnik zadao prilikom kreiranja alarma. Oglašavanje alarma mora trajati sve do uspješnog ispunjenja izazova.                                                                                                                                                                                                             | 
| F05    | Obavještavanje korisnika o nadolazećem alarmu | Sat vremena prije oglašavanja alarma aplikacija će obavijestiti korisnika putem obavijesti sustava da će se alarm oglasiti za sat vremena te omogućiti korisniku da ugasi alarm.                                                                                                                                                                                                               | 
| F06    | Rješavanje matematičkog izazova               | Funkcionalnost se pokreće nakon oglašavanja alarm te traje sve do uspješno ispunjenog izazova od strane korisnika. Funkcionalnost zahtijeva od strane korisnika rješavanje određenog broja matematičkih zadataka kako bi ugasio alarm.                                                                                                                                                         | 
| F07    | Rješavanje Zmija izazova                      | Funkcionalnost se pokreće nakon oglašavanja alarm te traje sve do uspješno ispunjenog izazova od strane korisnika. Funkcionalnost zahtijeva od korisnika kontrolu kretanja zmije po određenim poljima te skupljanje jabuka pri čemu je potrebno paziti da glava zmije ne dotakne svoje tijelo. Od korisnika se zahtijeva da pojede, odnosno skupi određenu brojku jabuka kako bi ugasio alarm. |

## Tehnologije i oprema

Aplikacija se razvija koristeći programski jezik Kotlin u razvojnom okruženju Android Studio. Od GIT-a koristi se platforma GitHub za verzioniranje programskog koda, GitHub Wiki za pisanje tehničke i projektne dokumentacija. Jira (Atlassian) alat je koji se koristi za organizaciju i agilno upravljanje projektom razvoja opisane aplikacije. Projektni tim primjenjuje Scrum agilnu metodu prilikom realizacije projekta. Za izradu skica i prototipa aplikacije koristi će se web aplikacija Marvel App.

Programski alati:

-   Android Studio (SDK-API 24, "Nougat")
-   Kotlin
-   GitHub
-   GitHub Wiki
-   Jira
-   Marvel App

Oprema:

-   Osobno računalo
