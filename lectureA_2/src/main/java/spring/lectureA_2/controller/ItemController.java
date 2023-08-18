package spring.lectureA_2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import spring.lectureA_2.domain.item.Album;
import spring.lectureA_2.domain.item.Book;
import spring.lectureA_2.domain.item.Item;
import spring.lectureA_2.domain.item.Movie;
import spring.lectureA_2.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 책 주문
     */
    @GetMapping("/items/newbook")
    public String createBookForm(Model model) {
        model.addAttribute("bookform", new BookForm());
        return "items/createBookItemForm";
    }

    @PostMapping("/items/newbook")
    public String createBook(BookForm bookForm) {
        Book book = new Book();
        //setter 전부 날리고 메소드로 따로 빼서 하는게 best
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());
        itemService.saveItem(book);

        return "redirect:/";
    }

    /**
     * 영화 주문
     */
    @GetMapping("/items/newmovie")
    public String createMovieForm(Model model) {
        model.addAttribute("movieform", new MovieForm());
        return "items/createMovieItemForm";
    }

    @PostMapping("/items/newmovie")
    public String createMovie(MovieForm movieForm) {
        Movie movie = new Movie();
        //setter 전부 날리고 메소드로 따로 빼서 하는게 best
        movie.setName(movieForm.getName());
        movie.setPrice(movieForm.getPrice());
        movie.setStockQuantity(movieForm.getStockQuantity());
        movie.setDirector(movieForm.getDirector());
        movie.setActor(movieForm.getActor());
        itemService.saveItem(movie);

        return "redirect:/";
    }

    /**
     * 앨범 주문
     */
    @GetMapping("/items/newalbum")
    public String createAlbumForm(Model model) {
        model.addAttribute("albumform", new AlbumForm());
        return "items/createAlbumItemForm";
    }

    @PostMapping("/items/newalbum")
    public String createAlbum(AlbumForm albumForm) {
        Album album=new Album();
        //setter 전부 날리고 메소드로 따로 빼서 하는게 best
        album.setName(albumForm.getName());
        album.setPrice(albumForm.getPrice());
        album.setStockQuantity(albumForm.getStockQuantity());
        album.setArtist(albumForm.getArtist());
        album.setEtc(albumForm.getEtc());
        itemService.saveItem(album);

        return "redirect:/";
    }


    /**
     * 주문 목록
     */
    @GetMapping("/items")
    public String itemList(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "/items/itemList";
    }

    /**
     * 상품 수정 -> 책, 영화, 앨범
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/items/book/{id}/edit")
    public String updateBookItemForm(@PathVariable("id") Long id, Model model) {
        Book item = (Book) itemService.findOne(id);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("bookform", form);
        return "items/updateBookItemForm";
    }

    @GetMapping("/items/movie/{id}/edit")
    public String updateMovieForm(@PathVariable("id") Long id, Model model) {
        Movie item = (Movie) itemService.findOne(id);

        MovieForm form = new MovieForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setDirector(item.getDirector());
        form.setActor(item.getActor());

        model.addAttribute("movieform", form);
        return "items/updateMovieItemForm";
    }

    @GetMapping("/items/album/{id}/edit")
    public String updateAlbumForm(@PathVariable("id") Long id, Model model) {
        Album item = (Album) itemService.findOne(id);

        Album form = new Album();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setArtist(item.getArtist());
        form.setEtc(item.getEtc());

        model.addAttribute("albumform", form);
        return "items/updateAlbumItemForm";
    }


    @PostMapping("/items/book/{id}/edit")
    public String updateBookItem(@ModelAttribute("bookform") BookForm form) {
        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @PostMapping("/items/movie/{id}/edit")
    public String updateMovieItem(@ModelAttribute("movieform") MovieForm form) {
        Movie movie = new Movie();
        movie.setId(form.getId());
        movie.setName(form.getName());
        movie.setPrice(form.getPrice());
        movie.setStockQuantity(form.getStockQuantity());
        movie.setDirector(form.getDirector());
        movie.setActor(form.getActor());

        itemService.saveItem(movie);
        return "redirect:/items";
    }

    @PostMapping("/items/album/{id}/edit")
    public String updateAlbumItem(@ModelAttribute("albumform") AlbumForm form) {
        Album album = new Album();
        album.setId(form.getId());
        album.setName(form.getName());
        album.setPrice(form.getPrice());
        album.setStockQuantity(form.getStockQuantity());
        album.setArtist(form.getArtist());
        album.setEtc(form.getEtc());

        itemService.saveItem(album);
        return "redirect:/items";
    }

}

