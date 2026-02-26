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

async function createPayment() {
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
        showToast("Erro ao realizar pagamento", "error");
        return;
    }

    showToast("Pagamento enviado para análise!", "success");

    document.getElementById("amount").value = "";
    document.getElementById("description").value = "";
}