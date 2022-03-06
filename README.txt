Pentru inceput imi initializez W si outFile cu args[0] si args[2] adica numarul de workeri care vreau sa lucreze pentru rezultat si fisierul in care vreau sa scriu rezultatul final.
Pentru a citi fisierele pe care lucrez, am folosit un BufferReader care m-a ajutat sa citesc linie cu line. Astfel, am descis fisierul in fis, am citit prima linie care reprezenta numarul de caractere pe care trebuie sa il citesc, adica dimensiune_fragment, dupa  acest lucru, urmatoarea linie reprezinta numarul de file-uri pe care trebuie sa lucrez si dupa intr-un while, cat timp am ce sa citesc citesc fisierele si le salvez in lista files care e de tip String.
-----crearea taskruilor de tip map
Dupa ce am salvat fisierele si stiu pe ce lucrez, incep sa imi creez taskurile de tip map. Mi-am creat o clasa TaskMap in care mi-am pus numele fisierului, offset-ul si numarul de caractere pe care trebuie sa il citeasca. Astfel, am parcurs toate fisierele pe care lucrez cu un for, si pentru fiecare fisier citeam litera cu litera. Daca numarul literelor pe care le-am citit au ajuns sa fie egale cu numarul de chunkuri, faceam taskul de tip map, il adaugam la lista de taskuri, incrementam offset-ul si reluam numaratoarea. Acest lucru o faceam cat timp aveam ce sa citesc. In creearea taskurilor avem 2 posibilati:
-numarul de caractere pe care l-am citit este egal cu dimensiunea_fragment citita din fisier, si atunci creeam taskul map cu dimensiunea de chunck si desigur offset ul il incrementam cu chunck
-ori nu aveam cum sa citesc numarul de chunck-uri pentru ca poate eram la finalul fisierului, si atunci faceam taskul map cu dimensiunea disp si nu mai incrementam offset-ul pentru ca eram la final.
-----distribuirea task-urilor si pornirea workerilor
Dupa acest lucru, la distribuirea taskruilor m-am gandit in felul urmator, daca am 3 workeri spre exemplu si 6 taskuri, primul worker o sa ia taskuri din 3 in 3 adica
1 --
2   \
3    \
4----- primul worker
5
6
si tot asa astfel incat daca sa zicem ca sunt 3 workeri si 4 taskuri si teoretic 2 workeri nu o sa mai aiba de unde sa ia taksuri, o sa vina perfect pentru ca workerul 2 o sa faca 2(id-ul lui) + 3(numarul de workeri) = 5 si nu avem de unde dar 1(id-ul lui) + 3(numarul de workeri) = 4 si da avem aici.
Cand am creat thread-ul, i-am dat ca parametru o lista cu id-uri pentru ca el mai departe sa stie ce task lucreaza din lista creata mai sus /\ . 
------operatia de Map
In operatia de map am facut un for pentru a parcurge toate id-urile trimise ca parametru la thread si am facut urmatoarele lucruri:
-mi-am extras din din Lista de taskuri din main datele pentru lucru adica fisierul, offset-ul si dimensiunea fragmentuli pe care trebuie sa o citesc
-dupa m-am apucat sa citesc si sa lucrez
In functie de numarul pe care trebuie sa il citesc avem 2 posibilati. Ori trebuie sa citesc exact cat e dimensiunea_fragment citita din fisierul de in, si atunci e clar faptul ca nu am ajuns la finalul fisierului ori trebuie sa citesc mai putine si atunci e clar ca am ajuns la capatul fisierului.
Am realizat aceasta diferentiere intre cele 2 cazuri pentru ca daca am ajuns la capatul fisierului, e clar faptul ca nu am ce verificare sa fac dupa fragmentul pe care l-am citit pentru ca nu exista posibilatea ca fragmentul meu sa se incheie cu jumatate de cuvant.
Insa, daca cumva dimensiunea pe care trebuie sa citesc este egala cu dimensiunea_fragment, inseamna ca avem nevoie de 2 verificari, in fata sa vedem daca nu cumva exista un cuvant care se continua peste fragmentul nostru, dar si dupa fragment.
Astfel, daca offset-ul este diferit de 0, maresc chunck-ul pentru ca trebuie sa citesc cu o litera inainte de offset pentru verificarea fragmentului. 
===---daca offset ==0
Daca offset-ul este 0, asta inseamna ca trebuie sa verific doar dupa fragment.
Prin urmare, cu un for de la 0 la chunck, in linie imi salvez litera cu litera din fragment. Dupa ce termin fragmentul de citit, am salvat in ante ultimul caracter din fragment si in c am citit urmatorul caracter care teoretic imi sare de chunk. Daca si ante si c sunt litere, asta inseamna ca un cuvant se intinde peste urmatorul chunck, deci trebuie sa il citesc. Cu un cat timp c este litera, citesc in continuare pana cand nu mai este litera si mi se opreste.
====---daca offset !=0
Daca offset-ul este diferit de 0,in ce citesc caracterul de dinaintea fragmentului meu si micsorez chunck-ul si in urm salvez cum ar veni primul caracter din fragmentul notru. daca si c si urm sunt litere, asta inseamna ca un cuvant este in desfasurare deci trebuie sa il sar. Astfel cu un cat timp c este litera, citesc pana dau de un spatiu si miscorez numarul de chunck. Practic citesc in gol ca sa scap de cuvant.
Dupa ce am facut verificarea, cu un for citesc fragmentul meu si dupa aceasta citire ca ami sus fac verificarea de dupa fragment. Astfel in ante salvez ultimul meu caracter din fragment si in c urmatoarea litera care teoretic e de dupa fragmentul meu. Daca si ante si c sunt litere asta inseamna ca exista un cuvant si il citesc si pe el cu un cat timp c este litera.
====---cazul in care am mai putine litere de citit(sfarsitul fisierului)
Daca am ajuns la sfarsitul fisierului, am de facut doar verificarea de dinainte, adica sa sar un cuvant daca cumva se intinde peste fragmentul meu. Astfel in c am salvat caracterul de dinaintea fragmentului pentru ca am dat skip la offset -1 si in urm am salvat primul caracter din fragmentul meu. Daca ambele sunt litere, citesc din nou in gol. Cu un for la final citesc ce a mai ramas din fragmentul meu.
=== numararea lungimilor cuvintelor si aparitiile lor
Dupa ce am terminat de creat fragmentul meu, il parcurg in vederea stabilirii aparitiile existente. Astfel, cu un for parcurg litera cu litera pe care o salvez in c. Daca c este litera, cresc lungimea si ok devine 0. Ok in acest caz este ca un fel de verificare care imi arata mie ca un cuvant este in curs de numarare. Daca cumva nu mai citesc litere deci intru pe else, verific daca exista deja lungimea in aparitii, daca exista ii incrementez valoarea, daca nu adaug lungimea respectiva cu numarul ei de aparitii initial 1 si ok =1. 
Am folosit variabila ok pentru ca exista posibilitatea ca sa avem un cuvant cum ar fi "Andrei" si dupa Andrei sa nu avem nimic. Daca dupa "Andrei" nu aveam nimic, teoretic numaratoarea a fost inceputa dar nu si incheiata pentru ca nu imi intra pe else, astfel dupa for mai fac un if ok ==0 sa vad daca mi-am incheiat orice numaratoare.
Desigur ca in tot acest timp am avut o variabila max = 0 care imi salva lungimea maxima. Inainte ca lungimea sa o adaug in vectorul de aparitii, verificam daca este mai mare decat maxim. Daca era, o salvam in maxim
=== salvarea cuvintelor de lungime maxima
Din nou cu un for am parcurs tot fragmentul, in c salvam caracterul, daca acesta era litera, numaram lungimea lui, desigur foloseam ok pentru motivele de mai sus si imi cream si un cuvant in String cuv.
Odata ce nu mai citeam litere, insemna ca se termina cuvantul si puteam sa compar lungimea lui cu max, daca aceata era egala cu max, salvam cuvantul in LONGS.
Dupa ce workerul termina task-ul, crea un Dictionar, clasa realizata cu campurile:
-filename(numele fisierului)
-aparitions(mapul de aparitii)
-longs(vectorul de cuvinte cu lungimea maxima)
Cu dictionarul creat pe task-ul respectiv, il adaugam intr-o lista din main, dicFinal . Am folosit un synchronized pentru ca nu vrem ca mai multi workeri sa puna in acelasi timp pentru ca ar fi race condition ca mai multe threaduri sa modifice aceasi variabila in acelasi timp.
-----Ne intoarcem in main
Inapoi in main, dupa operatia de map am trecut la operatia de reduce. 
Inainte de a porni threadurile cu Reduce ma asigur ca impart taskurile ok. Pe acelasi principiu ca la map. Iau id-ul threadului si adun la acesta numarul de fisiere. Astfel daca sa zicem avem 4 fisiere si 3 workeri, primul o sa ia 1(id-ul lui) + 3(numarul de fisiere) = 4 iar al doilea doar fisierul 2.Desigur in taskid am si memorat id-ul fisierului pentru care workerul realizeaza rezultatul final.
----operatia de reduce
Odata ce am inceput operatia de reduce, imi salvez in numberTask id-urile fisierelor pentru care eu trebuie sa realizez rezultatul. Cu un for parcurg taskrule pe care trebuie sa le fac. La fiecare pas, imi declar o mapa de paaritii unde o sa combin toate aparitiile din dictionarele in care lucrez, un vector in care o sa bag cuvintele cele mai lungi si myList in care o sa retin dictionarele pe care lucrez. 
Pentru inceput, pe primul task imi salvez numele fisierului corespunzator id-ului pe care il am. Spre exemplu:
eu in files am numele fisierelor adica in1.txt, in2.txt si in3.txt
in numberTasks regasim id-ul fisierelor pe care lucreaza

Astfel daca un worker regaseste in numberTask valoarea 0, el se va duce si va lucra pe files[0], pe fisierul cu id-ul 0

Dupa ce workerul nostru stie pe ce fisier lucreaza, sa zicem in1.txt, el se duce prin tot dicFinal unde avem toate dictionarele si ia dictionarele care au fileName-ul in1.txt. 
Dupa ce a luat toate dictionarele, parcurge aceste dictionare in prima faza pentru a combina mapurile de la toate dictionarele. Astfel cu un for parcurg lista mea de dictionare. Cand sunt pe primul dictionar spre exemplu, parcurg mapul lui de aparitii. Daca lungimea gasita in dictionar se regaseste in mapul WORKERULUI de aparitii, iau aparitiile respectivei lungimi si o adaug la valoarea pe care o avem in map. Daca nu o regasim in mapul WORKERULUI, adaugam lungimea si aparatia ei. Dupa acest lucru, mergem si pe urmatoarele dictionare.

Dupa ce am combinat mapruile, parcurg mapul workerilor unde am salvat mapul combinat pentru a alfa lungimea maxima pe care o avem.


Dupa ce am aflat lungime maxima, parcug lista mea de dictonare pentru a combina listele cu cuvintele cele mai lungi. Astfel parcurg Dictionarele si sa zicem ca ne orpim la primul. Salvez in longG lista de cuvinte din acest dictionar si vad in aceasta lista ce cuvant are lungimea egala cu max. Daca exista cuvinte le adaug in lista WORKERULUI.

Dupa aceste operatii, creez un dictionar nou in care o sa avem numele fisierului pentru care am facut operatia de Reduce, mapul WORKERULUI combinat si lista de cuvintele lungi. Desigur si acum folosesc un synchronized pentru a nu avea race condition. Acest dictionar o sa fie adaugat in dicFinalReduce.

---ne reintoarcem in main
In main in momentul in care ne-am intors, parcugem lista de dictionare finale adica cu mapurile si listele contopite si in aceasta parcurgere facem urmatoarele
1)aflam numarul de cuvinte prin adaugarea tuturor aparitiilor gasite
2)calculez rangul cu ajutorul functiei Fib pe care am implementat-o in main
3) vad aparitia cuvientelor care au lungimea cea mai mare
Dupa toate aceste 3 etape, am creat un FinalResult unde am:
-numele fisierului
-rangul
-lungimea celui mai lung cuvant
-aparitiile acestuia.

Pentru a ordona fisierele in ordine descrescatoare, am folosit un vector pe care l-am initializat in functie de cate finalResults am iar finalResult este egal cu cate fisiere am pentru ca am un final result pe fisier.
In vectorul meu v am introdus toate rangurile pentru a le ordona descrescator
am parcurs descrescator rangurile pentru a stii ce resultate finale sa afisez si in ce ordine. Astfel, parcurgand vectorul v, ne fixam pe un rang si afisam resultatul final cand il gaseam.
Pentru a afisa am folosit Random AccessFile. Am parcurs vectorul cu rangurile ordonate descrecator. Pentru fiecare rang am parcurs lista de resultate finale si cand gaseam rangul egal cu rangul din finalResult, afisam resultatul.




