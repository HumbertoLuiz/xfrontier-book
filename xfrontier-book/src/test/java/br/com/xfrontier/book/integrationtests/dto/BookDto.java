package br.com.xfrontier.book.integrationtests.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import br.com.xfrontier.book.core.enums.BookStatus;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonNaming(SnakeCaseStrategy.class)
public class BookDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String author;
	private String isbn;
	private LocalDate launchDate;
	private OffsetDateTime registrationDate;
	private OffsetDateTime updateDate;
	private Integer createdBy;
	private Integer lastModifiedBy;
	private Boolean active;
	private BookStatus bookStatus;
	private BigDecimal shippingRate;
	private BigDecimal price;
	private String observation;
	private String reasonCancellation;
    private Long categoryId;

		
	public BookDto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, author, bookStatus, categoryId, createdBy,
				id, isbn, lastModifiedBy, launchDate, observation, price,
				reasonCancellation, registrationDate, shippingRate, title,
				updateDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookDto other = (BookDto) obj;
		return Objects.equals(active, other.active)
				&& Objects.equals(author, other.author)
				&& bookStatus == other.bookStatus
				&& Objects.equals(categoryId, other.categoryId)
				&& Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(id, other.id)
				&& Objects.equals(isbn, other.isbn)
				&& Objects.equals(lastModifiedBy, other.lastModifiedBy)
				&& Objects.equals(launchDate, other.launchDate)
				&& Objects.equals(observation, other.observation)
				&& Objects.equals(price, other.price)
				&& Objects.equals(reasonCancellation, other.reasonCancellation)
				&& Objects.equals(registrationDate, other.registrationDate)
				&& Objects.equals(shippingRate, other.shippingRate)
				&& Objects.equals(title, other.title)
				&& Objects.equals(updateDate, other.updateDate);
	}

}