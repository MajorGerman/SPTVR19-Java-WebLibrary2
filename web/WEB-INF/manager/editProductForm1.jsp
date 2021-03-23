<h2 class="display-5"> Изменить товар </h2>   
<form action="editProductForm2" method="POST">
    <select id="idforedit" onchange="delete_update()" style="width: 55%;margin: auto;" name="productId" class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
        <c:forEach var="product" items="${listProducts}">
            <option value="${product.id}">${product.name} (${product.tags} | ${product.price}$)</option>
        </c:forEach>       
    </select>
    <button type="submit" class="btn btn-primary"> Изменить </button>    
</form>
<br>
<form action="deleteProduct" method="POST">
    <input id="idfordelete" name="productId" hidden>
    <button type="submit" class="btn btn-danger"> Удалить </button>
</form>

<script>
    function delete_update() {
        document.getElementById("idfordelete").value = document.getElementById("idforedit").value;
        console.log(document.getElementById("idfordelete").value);
    }
    
    var jopa = setInterval(delete_update, 10);
    
</script>
