package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@Entity
public class Product implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<String> tags;
    private String name;
    private int price;
    private int count;
    @OneToOne
    private Cover cover;
    private boolean access;
    private int discount;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date discountDate;
    private int discountDuration;


    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public Product(String name, int price, Cover cover, int count) {
        this.name = name;
        this.price = price;
        this.access = true;
        this.cover = cover;
        this.count = count;
    }
    
    public Product() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
        public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getDiscountDate() {
        return discountDate;
    }

    public void setDiscountDate(Date discountDate) {
        this.discountDate = discountDate;
    }

    public int getDiscountDuration() {
        return discountDuration;
    }

    public void setDiscountDuration(int discountDuration) {
        this.discountDuration = discountDuration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.tags);
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + this.price;
        hash = 79 * hash + this.count;
        hash = 79 * hash + Objects.hashCode(this.cover);
        hash = 79 * hash + (this.access ? 1 : 0);
        hash = 79 * hash + this.discount;
        hash = 79 * hash + Objects.hashCode(this.discountDate);
        hash = 79 * hash + this.discountDuration;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (this.price != other.price) {
            return false;
        }
        if (this.count != other.count) {
            return false;
        }
        if (this.access != other.access) {
            return false;
        }
        if (this.discount != other.discount) {
            return false;
        }
        if (this.discountDuration != other.discountDuration) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.tags, other.tags)) {
            return false;
        }
        if (!Objects.equals(this.cover, other.cover)) {
            return false;
        }
        if (!Objects.equals(this.discountDate, other.discountDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", tags=" + tags + ", name=" + name + ", price=" + price + ", count=" + count + ", cover=" + cover + ", access=" + access + ", discount=" + discount + ", discountDate=" + discountDate + ", discountDuration=" + discountDuration + '}';
    }
  
    
    
}
