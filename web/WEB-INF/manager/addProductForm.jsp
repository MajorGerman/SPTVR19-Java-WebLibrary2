<h2 class="display-5"> Добавить товар </h2>
<form action="addProduct" method="POST" enctype="multipart/form-data">
  <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Название</label>
        <input name="name" value="${name}" class="form-control" id="exampleInputPassword1">
    </div>
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Цена</label>
        <input name="price" type="number" min="1" value="${price}" class="form-control" id="exampleInputPassword1">
    </div>
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Категория</label>
        <input name="tag" value="${tag}" class="form-control" id="exampleInputPassword1">
    </div>    
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Описание</label>
        <input name="description" value="${description}" class="form-control" id="exampleInputPassword1">
    </div>    
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Обложка</label>
        <input name="file" type="file" class="form-control" id="exampleInputPassword1">
    </div>    
    <button type="submit" class="btn btn-primary"> Готово </button>
</form>
    
    
