package com.kill3rtaco.api.pagination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A pagination helper class
 * 
 * @author KILL3RTACO
 *
 * @param <T>
 *            The type of elements to paginate
 * @since TacoAPI/Pagination 1.0
 * @see PageViewer
 */
public class Paginator<T> implements Iterable<T> {
	
	private int		_elementsPerPage, _pages = 0;
	private List<T>	_elements	= new ArrayList<T>();
	
	/**
	 * Construct a new Paginator and set the elements shown per page.
	 * 
	 * @param elementsPerPage
	 *            elementsPerPage the maximum amount of elements per page
	 * @since TacoAPI/Pagination 1.0
	 */
	public Paginator(int elementsPerPage) {
		this(elementsPerPage, null);
	}
	
	/**
	 * 
	 * @param elementsPerPage
	 *            the maximum amount of elements per page
	 * @param elements
	 *            the elements that should be added immediately
	 * @since TacoAPI/Pagination 1.0
	 */
	public Paginator(int elementsPerPage, Collection<T> elements) {
		_elementsPerPage = elementsPerPage;
		appendAll(elements);
	}
	
	/**
	 * Calculate the total amount of pages for this Paginator object
	 * 
	 * @since TacoAPI/Pagination 1.0
	 */
	public void calculatePages() {
		_pages = _elements.size() / _elementsPerPage + (_elements.size() % _elementsPerPage != 0 ? 1 : 0);
	}
	
	/**
	 * Add an element.
	 * 
	 * @param element
	 *            The element to add
	 * @since TacoAPI/Pagination 1.0
	 */
	public void append(T element) {
		_elements.add(element);
		if (_pages == 0) {
			_pages++;
			return;
		}
		calculatePages();
	}
	
	/**
	 * Append all the elements in the given collection to this Paginator
	 * 
	 * @param append
	 *            The elements to append
	 * @since TacoAPI/Pagination 1.0
	 */
	public void appendAll(Collection<T> append) {
		if (append != null && !append.isEmpty()) {
			for (T t : append) {
				append(t);
			}
		}
	}
	
	/**
	 * Remove an element.
	 * 
	 * @param element
	 *            - The element to be removed
	 * @since TacoAPI/Pagination 1.0
	 */
	public void remove(T element) {
		if (_elements.contains(element))
			_elements.remove(element);
		calculatePages();
	}
	
	/**
	 * Get all the elements of this Paginator
	 * 
	 * @return the elements
	 * @since TacoAPI/Pagination 1.0
	 */
	public List<T> getElements() {
		return _elements;
	}
	
	/**
	 * Get a list of elements at a specific page
	 * 
	 * @param page
	 *            The page to get
	 * @return the elements
	 * @since TacoAPI/Pagination 1.0
	 */
	public List<T> getPage(int page) {
		if (hasNoPages())
			return new ArrayList<T>();
		else if (!hasPage(page))
			page = 1;
		List<T> list = new ArrayList<T>();
		for (int i = (page - 1) * _elementsPerPage; i < _elements.size(); i++) {
			list.add(_elements.get(i));
			if (list.size() == _elementsPerPage)
				return list;
		}
		return list;
	}
	
	/**
	 * Get how many elements should be listed per page
	 * 
	 * @return How many elements should be listed per page
	 * @since TacoAPI/Pagination 1.0
	 */
	public int getElementsPerPage() {
		return _elementsPerPage;
	}
	
	/**
	 * Set how many elements should be listed per page
	 * 
	 * @param elements
	 *            how many elements that should be listed per page
	 * @since TacoAPI/Pagination 1.0
	 */
	public void setElementsPerPage(int elements) {
		_elementsPerPage = elements;
	}
	
	/**
	 * Tests if there are no pages.
	 * 
	 * @return true if there are no pages
	 * @since TacoAPI/Pagination 1.0
	 */
	public boolean hasNoPages() {
		return _pages == 0;
	}
	
	/**
	 * Test if the page exists.
	 * 
	 * @param page
	 * @return true if the page exists
	 * @since TacoAPI/Pagination 1.0
	 */
	public boolean hasPage(int page) {
		return page > 0 && page <= _pages;
	}
	
	/**
	 * Get the amount of pages this PageBuilder has
	 * 
	 * @return the amount of pages
	 * @since TacoAPI/Pagination 1.0
	 */
	public int pages() {
		return _pages;
	}
	
	@Override
	public Iterator<T> iterator() {
		return _elements.iterator();
	}
	
}
