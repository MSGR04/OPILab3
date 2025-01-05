function getCurrentRValue() {
    var spinner = PF('radiusSpinner');
    return spinner ? parseFloat(spinner.getValue()) : null;
}

function onRadiusChange() {
    drawGraph();
    drawPreviousPoints();
}

document.getElementById("mainForm").onsubmit = function (e) {
    e.preventDefault();
    var yInput = document.getElementById("mainForm:y");
    var yValue = parseFloat(yInput.value);
    var rValue = getCurrentRValue();

    var isValid = true;
    var errorMessage = "";

    if (isNaN(yValue) || yValue < -5 || yValue > 5) {
        errorMessage += "Введите валидный Y (-5 ... 5)\n";
        isValid = false;
    }
    if (isNaN(rValue) || rValue < 1 || rValue > 3) {
        errorMessage += "Выберите R (1 ... 3)\n";
        isValid = false;
    }

    if (isValid) {
        e.target.submit();
    } else {
        alert(errorMessage);
    }
};

const canvas = document.getElementById('graphCanvas');
const ctx = canvas.getContext('2d');
const axisRange = 7;

function drawGraph() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const rValue = getCurrentRValue();

    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    const unitScale = canvas.width / axisRange;

    drawAxes(unitScale);
    drawLabels(unitScale);

    if (!rValue) {
        return;
    }

    ctx.fillStyle = 'rgba(0, 0, 255, 0.5)';
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(
        centerX,
        centerY,
        unitScale * rValue / 2,
        3/2 * Math.PI,
        2 * Math.PI,
        false
    );
    ctx.closePath();
    ctx.fill();

    ctx.fillRect(
        centerX - unitScale * rValue,
        centerY - unitScale * rValue,
        unitScale * rValue,
        unitScale * rValue
    );

    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.lineTo(centerX + unitScale * (rValue), centerY);
    ctx.lineTo(centerX, centerY + unitScale * (rValue) / 2);
    ctx.closePath();
    ctx.fill();
}

function drawAxes(unitScale) {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    ctx.strokeStyle = 'black';
    ctx.lineWidth = 1.5;
    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(canvas.width, centerY);
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, canvas.height);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(canvas.width - 10, centerY - 5);
    ctx.lineTo(canvas.width, centerY);
    ctx.lineTo(canvas.width - 10, centerY + 5);
    ctx.moveTo(centerX - 5, 10);
    ctx.lineTo(centerX, 0);
    ctx.lineTo(centerX + 5, 10);
    ctx.stroke();
}

function drawLabels(unitScale) {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    ctx.fillStyle = 'black';
    ctx.font = '12px Arial';

    ctx.textBaseline = 'middle';

    for (let i = -4; i <= 4; i++) {
        if (i === 0) continue;
        const x = centerX + i * unitScale;
        ctx.beginPath();
        ctx.moveTo(x, centerY - 7);
        ctx.lineTo(x, centerY + 7);
        ctx.stroke();

        let labelOffsetY = centerY + 15;

        ctx.textAlign = 'center';
        ctx.fillText(i.toString(), x, labelOffsetY);
    }

    for (let i = -4; i <= 4; i++) {
        if (i === 0) continue;
        const y = centerY - i * unitScale;
        ctx.beginPath();
        ctx.moveTo(centerX - 7, y);
        ctx.lineTo(centerX + 7, y);
        ctx.stroke();

        let labelOffsetX = centerX + 15;

        ctx.textBaseline = 'middle';
        ctx.fillText(i.toString(), labelOffsetX, y);
    }
}

function drawPoint(x, y, isHit) {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    const unitScale = canvas.width / axisRange;

    const pixelX = centerX + x * unitScale;
    const pixelY = centerY - y * unitScale;

    ctx.fillStyle = isHit ? `rgb(138, 255, 92)` : 'red';
    ctx.beginPath();
    ctx.arc(pixelX, pixelY, 3, 0, 2 * Math.PI);
    ctx.fill();
}

function drawPreviousPoints() {
    const table = document.getElementById("mainForm:resultsTable");
    const tbody = table.getElementsByTagName('tbody')[0];
    if (!tbody) return;
    const rows = tbody.rows;
    const selectedR = getCurrentRValue();
    if (!selectedR) {
        return;
    }

    for (let i = 0; i < rows.length; i++) {
        const x = parseFloat(rows[i].cells[0].innerText);
        const y = parseFloat(rows[i].cells[1].innerText);
        const r = parseFloat(rows[i].cells[2].innerText);
        const hitText = rows[i].cells[3].innerText.trim();
        const hit = hitText === "Да";
        if (selectedR === r) {
            drawPoint(x, y, hit);
        }
    }
}

canvas.addEventListener('click', function (event) {
    const rValue = getCurrentRValue();

    if (!rValue) {
        alert("Please, select R value");
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const unitScale = canvas.width / axisRange;

    let graphX = (x - centerX) / unitScale;
    let graphY = (centerY - y) / unitScale;

    checkPoint([{name: 'x', value: graphX.toFixed(2)}, {name: 'y', value: graphY.toFixed(2)}, {name: 'r', value: rValue}]);
});

function resizeCanvas() {
    canvas.width = canvas.offsetWidth;
    canvas.height = canvas.offsetHeight;
    drawGraph();
    drawPreviousPoints();
}

window.addEventListener('resize', resizeCanvas);
window.addEventListener('load', resizeCanvas);