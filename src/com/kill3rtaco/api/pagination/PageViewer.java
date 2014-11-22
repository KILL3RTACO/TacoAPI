package com.kill3rtaco.api.pagination;

/**
 * A class that extends Paginator to support the viewing of its elements
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Pagination 1.0
 * @see Paginator
 */
public abstract class PageViewer extends Paginator<String> {
	
	/**
	 * The default header format for a PageViewer.<br/>
	 * <br/>
	 * 
	 * @since TacoAPI/Pagination 1.0
	 */
	public static final String	DEF_HEADER_FORMAT	= "=====[%title %page]=====";
	
	private String				_headerFormat, _title, _subtitle = "";
	
	/**
	 * Construct a PageViewer object from a Paginator object. This constructor
	 * takes the elements from the provided paginator object and adds each
	 * element after calling .toString() on them
	 * 
	 * @param paginator
	 *            the paginator
	 * @since TacoAPI/Pagination 1.0
	 */
	public <T extends Object> PageViewer(Paginator<T> paginator) {
		super(paginator.getElementsPerPage());
		for (T t : paginator) {
			append(t.toString());
		}
	}
	
	/**
	 * Construct a new PageViewer with the given title and default header
	 * format.
	 * 
	 * @param title
	 *            the title of the PageViewer
	 * @since TacoAPI/Pagination 1.0
	 */
	public PageViewer(String title) {
		this(title, "");
	}
	
	/**
	 * Construct a new PageViewer with the given title and header format
	 * 
	 * @param title
	 *            the title of the PageViewer
	 * @param headerFormat
	 *            the format of the header.
	 *            <ul>
	 *            <li>%title will be replaced with the title</li>
	 *            <li>%page will be replaced with "Page {pageNum}/{totalPages}"</li>
	 *            </ul>
	 * @since TacoAPI/Pagination 1.0
	 */
	public PageViewer(String title, String headerFormat) {
		this(title, headerFormat, null);
	}
	
	public PageViewer(String title, String headerFormat, String subtitle) {
		super(5);
		_title = title;
		_headerFormat = headerFormat;
		_subtitle = subtitle;
	}
	
	/**
	 * Set the subtitle. The subtitle is display under the title.
	 * 
	 * @param subtitle
	 *            the subtitle to set. Supply null or an empty string to remove
	 *            the current subtitle
	 */
	public void setSubtitle(String subtitle) {
		_subtitle = subtitle;
	}
	
	/**
	 * Get the subtitle of this PageViewer
	 * 
	 * @return the subtitle
	 * @since TacoAPI/Pagination 1.0
	 */
	public String getSubtitle() {
		return _subtitle;
	}
	
	public abstract void showPage(int page);
	
	/**
	 * Set the title of this PageViewer
	 * 
	 * @param title
	 *            the new title
	 * @since TacoAPI/Pagination 1.0
	 */
	public void setTitle(String title) {
		_title = title;
	}
	
	protected String makeHeader(int page) {
		return _headerFormat.replace("%title", _title).replace("%page", "Page " + page + "/" + pages());
	}
	
	protected abstract void print(String message);
	
}
