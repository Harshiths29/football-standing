import React, { useState } from "react";

const StandingForm = () => {
  const [country, setCountry] = useState("");
  const [league, setLeague] = useState("");
  const [team, setTeam] = useState("");
  const [result, setResult] = useState("");
  const [error, setError] = useState("");
  const [mode, setMode] = useState("online");

  const fetchData = async (e) => {
    e.preventDefault();
    setResult("Loading...");
    setError("");

    try {
      const res = await fetch("http://localhost:8080/api/football/standings", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ country, league, team }),
      });

      if (!res.ok) {
        throw new Error(await res.text());
      }

      const data = await res.json();
      const teams = data._embedded?.responseDTOList || [];

      if (teams.length === 0) {
        setResult("No standings found.");
        return;
      }

      const html = teams
        .map(
          (t) => `
        <div style="padding: 10px; border: 1px solid #ccc; margin-bottom: 1rem;">
          <strong>Country:</strong> ${t.countryId ? `(${t.countryId})` : "N/A"} - ${t.countryName}<br/>
          <strong>League:</strong> (${t.leagueId}) - ${t.leagueName}<br/>
          <strong>Team:</strong> (${t.teamId}) - ${t.teamName}<br/>
          <strong>Position:</strong> ${t.position}
        </div>`
        )
        .join("");
      setResult(html);
    } catch (err) {
      setError(err.message);
      setResult("");
    }
  };

  const toggleMode = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/football/toggle-mode");
      const message = await res.text();
      setMode(message.includes("true") ? "offline" : "online");
    } catch (err) {
      setError("Failed to toggle mode.");
    }
  };

  return (
    <>
      <form onSubmit={fetchData}>
        <label>Country:</label>
        <input value={country} onChange={(e) => setCountry(e.target.value)} required />

        <label>League:</label>
        <input value={league} onChange={(e) => setLeague(e.target.value)} required />

        <label>Team:</label>
        <input value={team} onChange={(e) => setTeam(e.target.value)} required />

        <button type="submit" style={{ marginTop: "10px" }}>Get Standings</button>
        <button type="button" onClick={toggleMode} style={{ marginLeft: "10px" }}>
          Toggle Mode (Current: {mode})
        </button>
      </form>

      <div style={{ marginTop: "20px", color: "green" }} dangerouslySetInnerHTML={{ __html: result }} />
      {error && <div style={{ color: "red" }}>{error}</div>}
    </>
  );
};

export default StandingForm;
