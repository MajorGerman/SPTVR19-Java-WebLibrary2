class ProductModule{
    
  async printAddProductForm(){
    document.getElementById('context').innerHTML=
    `
    <div class='text-center justify-content-center m-2'>
        <h2 class="display-5"> Добавить товар </h2>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Название</label>
            <input name="name" class="form-control" id="name">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Цена</label>
            <input name="price" type="number" min="1" class="form-control" id="price">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Категория</label>
            <input name="tag" class="form-control" id="tag">
        </div>    
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Описание</label>
            <input name="description" class="form-control" id="description">
        </div>    
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Обложка</label>
            <input name="file" type="file" class="form-control" id="photo">
        </div>    
        <br>
        <button id="addButton" type="submit" class="btn btn-primary"> Готово </button>
    </div>
    `;
  }
  
  async printBuyProductForm() {
      document.getElementById('context').innerHTML = `
        <div class='container-fluid'>
            <div class='row justify-content-xl-center'>           
          
            </div>
        </div>`;

        const response = await fetch('printBuyProductFormJson',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
                }});

        var result = await response.json();
            if (response.ok){
              console.log("Request status: "+result.requestStatus);
            } else {
              console.log("Ошибка получения данных");
            }
        console.log(result)
        for (let product of result) {
            document.getElementById('context').innerHTML += `
                <div class="col">
                    <input type="text" name="productId" value="${product.id}" hidden>                
                    <div class="card" style="width: 18rem;">
                    <img style="width: 95%;" src="insertFile/${product.cover.path}" class="card-img-top" alt="product">
                    <div class="card-body">
                      <h5 class="card-title">${product.name}</h5>
                      <p class="card-text">${product.cover.description}</p>
                      <p class="card-text">${product.price}$</p>
                      <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
                        Купить
                      </button>
                        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                          <div class="modal-dialog">
                            <div class="modal-content">
                              <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel"> Подтверждение </h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                              </div>
                              <div class="modal-body">
                                Вы уверены, что хотите купить товар "${product.name}" за ${product.price}$?
                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">  Нет  </button>
                                <button type="submit" class="btn btn-primary">  Да </button>
                              </div>
                            </div>
                          </div>
                        </div>
                    </div>
                   </div>
                  </div>`;
          }

    
  }
  
  
}
const productModule = new ProductModule();
export {productModule};