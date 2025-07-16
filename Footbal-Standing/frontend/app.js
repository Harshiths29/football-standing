document.getElementById('toggleModeBtn').addEventListener('click', async function () {
    const toggleStatus = document.getElementById('toggleStatus');
    toggleStatus.innerText = 'Toggling mode...';

    try {
        const response = await fetch('http://localhost:8080/api/football/toggle-mode');

        if (!response.ok) {
            throw new Error('Failed to toggle mode');
        }

        const message = await response.text();
        toggleStatus.innerText = `✅ ${message}`;
    } catch (error) {
        toggleStatus.innerText = `❌ ${error.message}`;
    }
});

document.getElementById('standingForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const country = document.getElementById('country').value;
    const league = document.getElementById('league').value;
    const team = document.getElementById('team').value;

    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = 'Loading...';

    try {
        const response = await fetch('http://localhost:8080/api/football/standings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ country, league, team })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error: ${errorText}`);
        }

        const data = await response.json();

        const standings = data._embedded?.responseDTOList || [];
        if (standings.length === 0) {
            resultDiv.innerHTML = "<span>No standings found.</span>";
            return;
        }

        resultDiv.innerHTML = standings
            .map((team) => {
                const countryInfo = team.countryId && team.countryId.trim() !== ''
                    ? `<strong>Country:</strong> (${team.countryId}) - ${team.countryName}<br/>`
                    : `<strong>Country:</strong> ${team.countryName}<br/>`;

                return `
      <div class="card">
        ${countryInfo}
        <strong>League:</strong> (${team.leagueId}) - ${team.leagueName}<br/>
        <strong>Team:</strong> (${team.teamId}) - ${team.teamName}<br/>
        <strong>Overall Position:</strong> ${team.position}
      </div>
      <hr/>
    `;
            }).join('');
    } catch (error) {
        resultDiv.innerHTML = `<span style="color:red;">${error.message}</span>`;
    }
});
