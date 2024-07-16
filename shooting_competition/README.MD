## Zawody Strzeleckie
Witamy na Dorocznych Zawodach Strzeleckich Bajtocji. Każdy z zawodników strzela do tarczy
w kształcie prostokąta. Tarcza jest podzielona na r wierszy i c kolumn, czyli składa się z r × c
kwadratowych pól. Każde z pól tarczy jest zamalowane na czarno lub biało. Wiadomo, że w
każdej kolumnie tarczy są dokładnie dwa pola białe i r −2 czarne. Wiersze tarczy numerujemy
od góry w dół kolejnymi liczbami naturalnymi 1 , . . . , r. Kolumny tarczy numerujemy od lewej
do prawej liczbami 1 , . . . , c. Każdy z zawodników strzela serię złożoną z c strzałów. Seria c
strzałów jest poprawna jeżeli w jej trakcie trafiono dokładnie po jednym białym polu w każdej
kolumnie i nie pozostał żaden wiersz, w którym nie byłoby trafionego białego pola. Pomóż
zawodnikowi znaleźć poprawną serię strzałów, o ile taka seria istnieje.

### Zadanie
Napisz program, który:
- wczytuje liczbę bloków danych z pliku tekstowego SHO.IN; każdy blok zawiera kompletny
opis jednej tarczy;
- następnie dla każdego bloku
  * wczytuje rozmiar tarczy i rozkład białych pól na tarczy;
  * sprawdza, czy istnieje poprawna seria strzałów dla tej tarczy; jeśli tak, to znajduje
jedną z nich;
  * zapisuje wynik w pliku tekstowym SHO.OUT;

### Wejście
W pierwszym wierszu pliku danych SHO.IN podana jest liczba bloków danych (opisów tarcz)
x, 1 ¬ x ¬ 5 . W kolejnych wierszach opisanych jest x bloków. Opis pierwszego bloku rozpo-
czyna się w drugim wierszu pliku wejściowego, a opis każdego kolejnego bloku rozpoczyna się
w wierszu bezpośrednio następującym po zakończeniu opisu poprzedniego bloku.
Opis każdego bloku (tarczy) rozpoczyna się od wiersza zawierającego dwie liczby całkowite
r i c (2 ¬ r ¬ c ¬ 1000 ) oddzielone pojedynczym odstępem. Liczby te oznaczają odpowiednio
liczbę wierszy i kolumn tarczy. Każdy z kolejnych c wierszy bloku (opisu tarczy) zawiera po
dwie liczby oddzielone pojedynczym odstępem. Liczby w ( i + 1)-szym wierszu bloku (1 ¬ i ¬ c)
oznaczają pozycje (tzn. numery wierszy na planszy) białych pól w i-tej kolumnie tarczy.
### Wynik
Dla i-tego bloku, 1 ¬ i ¬ x, Twój program powinien zapisać w i-tym wierszu pliku wynikowego
SHO.OUT ciąg c numerów wierszy (oddzielonych pojedynczymi spacjami) tworzących opis
poprawnej serii strzałów w białe pola kolejnych kolumn 1 , 2 , . . . , c. Jeżeli dla podanej tarczy
poprawna seria nie istnieje, w i-tym wierszu pliku SHO.OUT należy wpisać słowo NO.
### Przykład
Dla pliku tekstowego SHO.IN:
````
2
4 4
2 4
3 4
1 3
1 4
5 5
1 5
2 4
3 4
2 4
2 3
````
jedną z poprawnych odpowiedzi jest plik tekstowy SHO.OUT:
````
2 3 1 4
NO
````