import React, { useState } from 'react';
import './App.css';

function App() {
    const [country, setCountry] = useState('');
    const [league, setLeague] = useState('');
    const [team, setTeam] = useState('');
    const [result, setResult] = useState('');
    const [toggleMessage, setToggleMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setResult('Loading...');

        try {
            const response = await fetch('http://localhost:8080/api/football/standings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ country, league, team })
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(error);
            }

            const data = await response.json();
            const standings = data._embedded?.responseDTOList || [];

            if (standings.length === 0) {
                setResult('No standings found.');
                return;
            }

            const rendered = standings.map((team, i) => {
                const countryText = team.countryId?.trim()
                    ? `<strong>Country:</strong> (${team.countryId}) - ${team.countryName}<br/>`
                    : `<strong>Country:</strong> ${team.countryName}<br/>`;

                return `
          <div class="card">
            ${countryText}
            <strong>League:</strong> (${team.leagueId}) - ${team.leagueName}<br/>
            <strong>Team:</strong> (${team.teamId}) - ${team.teamName}<br/>
            <strong>Overall Position:</strong> ${team.position}
          </div><hr/>`;
            }).join('');

            setResult(rendered);
        } catch (err) {
            setResult(`<span style="color:red;">${err.message}</span>`);
        }
    };

    const toggleMode = async () => {
        setToggleMessage('Toggling mode...');
        try {
            const response = await fetch('http://localhost:8080/api/football/toggle-mode');
            const msg = await response.text();
            if (!response.ok) throw new Error(msg);
            setToggleMessage(`✅ ${msg}`);
        } catch (err) {
            setToggleMessage(`❌ ${err.message}`);
        }
    };

    return (
        <div className="container">
            <h1>Football Standings Checker</h1>

            <div className="toggle-container">
                <button id="toggleModeBtn" onClick={toggleMode}>Toggle Online/Offline Mode</button>
                <p id="toggleStatus">{toggleMessage}</p>
            </div>

            <form onSubmit={handleSubmit}>
                <label htmlFor="country">Country:</label>
                <input id="country" value={country} onChange={(e) => setCountry(e.target.value)} required />

                <label htmlFor="league">League:</label>
                <input id="league" value={league} onChange={(e) => setLeague(e.target.value)} required />

                <label htmlFor="team">Team:</label>
                <input id="team" value={team} onChange={(e) => setTeam(e.target.value)} required />

                <button type="submit">Get Standings</button>
            </form>

            <div className="result" dangerouslySetInnerHTML={{ __html: result }} />
        </div>
    );
}

export default App;
