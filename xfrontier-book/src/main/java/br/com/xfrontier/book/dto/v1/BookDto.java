package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import br.com.xfrontier.book.config.OffsetDateTimeDeserializer;
import br.com.xfrontier.book.core.enums.BookStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "title", "author", "isbn", "launchDate", "registrationDate", 
	"updateDate", "createdBy", "active", "price", "observation", "reasonCancellation", "category_id"})
public class BookDto extends RepresentationModel<BookDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	
	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String title;
	
	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String author;
	
	@NotBlank(message = "{not.blank.message}")
	@Size(min = 13, max = 13, message = "{size.message}")
	private String isbn;

	@NotNull(message = "{not.null.message}")
	@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate launchDate;
	
	@NotNull(message = "{not.null.message}")
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	private OffsetDateTime registrationDate;

	@NotNull(message = "{not.null.message}")
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	private OffsetDateTime updateDate;
	
	@NotNull(message = "{not.null.message}")
	private Integer createdBy;

	private Integer lastModifiedBy;
	
	@NotNull(message = "{not.null.message}")
	private Boolean active;
	
	@NotNull(message = "{not.null.message}")
	private BookStatus bookStatus;

	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = true, message = "{positive.message}")
	private BigDecimal shippingRate;
	
	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = true, message = "{positive.message}")
	private BigDecimal price;

	private String observation;

	private String reasonCancellation;
		
    @JsonProperty("category_id")
    private Long categoryId;

	@JsonIgnore
    private OrderDto order;

    @JsonIgnore
    private Set<PaymentMethodDto> paymentMethods = new HashSet<>();
    
    @JsonIgnore
    private Set<UserDto> responsible = new HashSet<>();

    @JsonIgnore
    private Set<ProductDto> products = new HashSet<>();
 
	public boolean acceptPaymentForm(PaymentMethodDto paymentMethod) {
		return getPaymentMethods().contains(paymentMethod);
	}
	
	public boolean doesntAcceptPaymentForm(PaymentMethodDto paymentMethod) {
		return !acceptPaymentForm(paymentMethod);
	}

	public BookDto() {}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public LocalDate getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(LocalDate launchDate) {
		this.launchDate = launchDate;
	}

	public OffsetDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(OffsetDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public OffsetDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(OffsetDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public BookStatus getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(BookStatus bookStatus) {
		this.bookStatus = bookStatus;
	}

	public BigDecimal getShippingRate() {
		return shippingRate;
	}

	public void setShippingRate(BigDecimal shippingRate) {
		this.shippingRate = shippingRate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getReasonCancellation() {
		return reasonCancellation;
	}

	public void setReasonCancellation(String reasonCancellation) {
		this.reasonCancellation = reasonCancellation;
	}

	public OrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderDto order) {
		this.order = order;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Set<PaymentMethodDto> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(Set<PaymentMethodDto> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public Set<UserDto> getResponsible() {
		return responsible;
	}

	public void setResponsible(Set<UserDto> responsible) {
		this.responsible = responsible;
	}

	public Set<ProductDto> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductDto> products) {
		this.products = products;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(active, author, bookStatus,
				categoryId, createdBy, isbn, key, lastModifiedBy, launchDate,
				observation, order, paymentMethods, price, products,
				reasonCancellation, registrationDate, responsible, shippingRate,
				title, updateDate);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookDto other = (BookDto) obj;
		return Objects.equals(active, other.active)
				&& Objects.equals(author, other.author)
				&& bookStatus == other.bookStatus
				&& Objects.equals(categoryId, other.categoryId)
				&& Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(isbn, other.isbn)
				&& Objects.equals(key, other.key)
				&& Objects.equals(lastModifiedBy, other.lastModifiedBy)
				&& Objects.equals(launchDate, other.launchDate)
				&& Objects.equals(observation, other.observation)
				&& Objects.equals(order, other.order)
				&& Objects.equals(paymentMethods, other.paymentMethods)
				&& Objects.equals(price, other.price)
				&& Objects.equals(products, other.products)
				&& Objects.equals(reasonCancellation, other.reasonCancellation)
				&& Objects.equals(registrationDate, other.registrationDate)
				&& Objects.equals(responsible, other.responsible)
				&& Objects.equals(shippingRate, other.shippingRate)
				&& Objects.equals(title, other.title)
				&& Objects.equals(updateDate, other.updateDate);
	}

}
