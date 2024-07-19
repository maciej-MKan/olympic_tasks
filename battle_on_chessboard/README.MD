## Aplikacja serwerowa do gry/symulacji bitwy na szachownicy.

### Założenia/reguły gry:
- pole do bitwy: szachownica o wymiarach definiowanych w konfiguracji
- na szachownicy jest dwóch przeciwników: biali i czarni
- na każdym polu jednocześnie może znajdować się jeden pojazd/jednostka
- możliwe typy jednostek:

  a) łucznik - możliwe komendy: strzel o n pól w lewo/prawo/dół/góra; przejdź o jedno pole: góra/dół/lewo/prawo
  
  b) transport - możliwe komendy: jedź o 1,2 lub 3 pola: góra/dół/lewo/prawo
  
  c) armata - możliwe komendy: strzel n pól w lewo/prawo i m w gór/dół - może strzelać po skosie
- gracz poprzez API WebService może wykonywać komendy najszybciej po określonym odstępie czasu od ostatniej komendy
- odstęp czasu po ostatniej komendzie zależny jest od komendy:

  a) ruch łucznika 5s

  b) ruch transportu 7s

  c) strzał łucznika 10s

  d) strzał armaty 13s
- jeżeli strzał trafi w jednostkę, czy to przeciwnika czy swoją, jednostka ulega zniszczeniu
- jeżeli pojazd najedzie na jednostkę przeciwnika, ulega ona zniszczeniu
- pojazd nie może najechać na swoją jednostkę, jeśli taka komenda zostanie wykonana, pojazd zostaje w miejscu ale odstęp czasowy do następnej jednostki zostaje zachowany

### Początek gry:
- jednostki zostają losowo rozmieszczane na szachownicy zgodnie z konfiguracją: np. 4 łuczników, 2 armaty, 5 transportów

### Komendy wykonywane przez WebService:
- lista jednostek (z informacją: ID jednostki, pozycja, typ jednostki, status - czy zniszczona czy aktywna, liczba wykonanych ruchów itp.)
- komenda dla jednostki z dokładnymi danymi (np. gdzie strzelić, gdzie jechać)
- komenda dla jednostki z rozkazem przypadkowego ruchu (aplikacja serwerowa wykonuje losową komendę)
- nowa gra - tworzy nową grę usuwając poprzednią

### Założenia API i aplikacji:
- dla każdej metod api (poza komendą nowej gry, jedną z informacji wejściowych jest informacja czy funkcja dotyczy białych czy czarnych)
- w przypadku gdy funkcja zwraca dane (np, lista jednostek), dane zwracane w json
- obaj przeciwnicy mogą wykonywać w dowolnym czasie komendy (z zachowaniem ww. odstępów czasowych), tj. komendy mogą być wydane "jednocześnie" - hazard !
- wykonywane komendy to operacje atomowe
- aplikacja zapisuje w bazie aktualny stan jak i historię komend oraz położenia jednostek dla obecnej i każdej poprzednio utworzonej gry


### Technologia:
- Java Spring
- REST API
- JPA
- Baza danych - dowolna (może być inmemory)
- Projekt maven lub gradle
- Testy jednostkowe
- OOP 