[![Build Status](https://travis-ci.com/swes42/valgfag4sem_security.svg?branch=main)](https://travis-ci.com/swes42/valgfag4sem_security)



- Modificeret addUser metoden, prøv at se om den giver meningen, det gjorde det nemlig i mit hovedet da jeg lavede det

Rest USERRESOURCE:
- ændret U_FACADE til USER_FACADE, bare så det gav mere mening
- lavet GET metode, linje 48-57 (henter metode fra JWTAuthen(...) og UserPrincipal (getEmail)

Security, JWTAUTHEN(...)
- Linje 72: Jeg ændrede metoden fra private til public, så jeg kunne få lov at bruge den i UserResource
- Linje 82,førhen stod der "roles", rettede til "role"

Security, UserPrincipal
- tilføjede linje 28, metode getEmail

Utils, SetupTestUsers
- linje 29 kopieret og udkommenteret
- linje 28 som er kopi af linje 29 uden both sætningen. Bare for at lige få compile success.

security, LoginEndpoint
- Udkommenteret linje/afsnit 47-55
- tilføjelser på linje 43 + 44
- udkommenteret linje 60

Mangler: HttpUtils klasse ?? er tilføjet, barre kopieret over fra gammel projekt

Jeg får fejl der retter sig mod noget med role. Men jeg tror vi er ved at være tæt på at have løst det - håber Daniel kan hjælpe med det imorgen!:D

