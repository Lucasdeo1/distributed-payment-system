function translateStatus(status) {
    const statusMap = {
        "PENDING": "PENDENTE",
        "UNDER_REVIEW": "EM REVISÃO",
        "APPROVED": "APROVADO",
        "REJECTED": "REJEITADO"
    };

    return statusMap[status] || status;
}


function showToast(message, type = "info") {
    const container = document.getElementById("toastContainer");

    const toast = document.createElement("div");
    toast.className = `toast toast-${type}`;
    toast.innerText = message;

    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3000);
}


/*async function createPayment() {
    const amount = document.getElementById("amount").value;
    const description = document.getElementById("description").value;

    const response = await fetch("/payments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            amount: parseFloat(amount),
            description: description
        })
    });

    if (!response.ok) {
        showToast("Erro ao criar pagamento", "error");
        return;
    }

    showToast("Pagamento criado com sucesso!", "success");

    document.getElementById("amount").value = "";
    document.getElementById("description").value = "";

    loadPayments();
}*/

async function loadPayments() {
    const response = await fetch("/payments");
    const payments = await response.json();

    const list = document.getElementById("paymentsList");
    list.innerHTML = "";

    payments.forEach(payment => {

        const item = document.createElement("div");
        item.className = "payment-card payment-status-" + payment.status;

        let details = `
            <strong>ID:</strong> ${payment.id}<br>
            <strong>Descrição:</strong> ${payment.description}<br>
            <strong>Valor:</strong> R$ ${payment.amount}<br>
            <strong>Status:</strong> ${translateStatus(payment.status)}<br>
            <strong>Pontuação de risco:</strong> ${payment.riskScore ?? 0}<br>
            <strong>Regras acionadas:</strong> ${(payment.riskReasons || []).join(", ") || "Nenhuma"}<br>
        `;

        item.innerHTML = details;

        // Pendente → pode analisar
        if (payment.status === "PENDING") {
            item.innerHTML += `
                <button onclick="analyzePayment('${payment.id}')">Analisar</button>
            `;
        }

        // em revisão → pode decidir
        if (payment.status === "UNDER_REVIEW") {
            item.innerHTML += `
                <button onclick="autoDecide('${payment.id}')">Decisão automática</button>
                <button onclick="approvePayment('${payment.id}')">Aprovar</button>
                <button onclick="rejectPayment('${payment.id}')">Rejeitar</button>
            `;
        }

        list.appendChild(item);
    });
}

async function analyzePayment(id) {
    const response = await fetch(`/payments/${id}/analyze`, {
        method: "POST"
    });

    if (!response.ok) {
        showToast("Erro na análise do pagamento", "error");
        return;
    }

    
    loadPayments();
}

async function autoDecide(id) {
    const response = await fetch(`/payments/${id}/auto-decide`, {
        method: "POST"
    });

    if (!response.ok) {
        showToast("Erro na decisão automática", "error");
        return;
    }

    showToast("Decisão automática concluída!", "success");
    loadPayments();
}

async function approvePayment(id) {
    const response = await fetch(`/payments/${id}/approve`, {
        method: "POST"
    });

    if (!response.ok) {
        showToast("Erro ao aprovar pagamento", "error");
        return;
    }

    showToast("Pagamento aprovado manualmente!", "success");
    loadPayments();
}

async function rejectPayment(id) {
    const response = await fetch(`/payments/${id}/reject`, {
        method: "POST"
    });

    if (!response.ok) {
        showToast("Erro ao rejeitar pagamento", "error");
        return;
    }

    showToast("Pagamento rejeitado manualmente!", "info");
    loadPayments();
}

window.onload = loadPayments;