<h2 class="display-5"> Изменить товар </h2> 
    <form action="editProduct" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="productId" value="${product.id}">
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Название</label>
            <input name="name" value="${product.name}" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Цена</label>
            <input name="price" type="number" min="1" value="${product.price}" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Категория</label>
            <input name="tags" value="${product.tags}" class="form-control" id="exampleInputPassword1">
        </div>    
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Описание</label>
            <input name="description" value="${product.cover.description}" class="form-control" id="exampleInputPassword1">
        </div>    
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Обложка</label>
            <input name="file" type="file" class="form-control" id="exampleInputPassword1">
        </div>    
        <button type="submit" class="btn btn-primary"> Готово </button>
    </form>