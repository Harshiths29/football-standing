# Football Standings Microservice

This Spring Boot microservice provides football team standings by country, league, and team name. It supports both online and offline modes with environment-based toggles.

##  Features
- Search standings by Country, League, and Team
- REST API with HATEOAS support
- Offline mode with toggle support
- Dockerized and CI/CD ready
- OpenAPI documentation (Swagger)

##  Technologies
- Java 21 + Spring Boot
- Spring Web, Validation, HATEOAS
- OpenAPI (Swagger UI)
- Docker, Jenkins

##  API Usage
### POST `/api/football/standings`
### We can change the config in application property 
### API Usage for change online/offline
### GET /api/football/toggle-mode 
For offline app.offline-mode=true use below details as input
```json
{
  "country": "England",
  "league": "Premier League",
  "team": "Manchester City"
}
```

For online app.offline-mode=false use below details as input
```json
{
  "country": "England",
  "league": "Non League Premier",
  "team": "Merthyr Town"
}
```
###  How to Run the UI

After starting the backend API:

1. Navigate to the `frontend` directory Footbal Standing\football-standings-ui>.
2. Then run command npm install after execution is completed run npm start and use this URL http://localhost:3000/ .
   OR
3. Open the file `index.html` using any browser (e.g., Chrome, Firefox).
4. Fill in the **Country**, **League**, and **Team** fields.
5. Click **Submit** to view the standings result.    make file 1 file 2 single file
