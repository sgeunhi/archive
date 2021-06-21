import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * <p>
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를
 * 유지하는 데이터베이스이다.
 */
public class MovieDB {

    MyLinkedList<Genre> genreList;

    public MovieDB() {
        genreList = new MyLinkedList<>();
        // FIXME implement this

        // HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한
        // MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }

    public void insert(MovieDBItem item) {
        int g = 0;
        int m = 0;
        MyLinkedListIterator<Genre> genreIt = new MyLinkedListIterator<>(genreList);
        if (!genreList.isEmpty()) {
            while (genreIt.hasNext()) {
                Genre nextGenre = genreIt.next();
                g = item.getGenre().compareTo(nextGenre.genreName);
                if (g < 0) {
                    Genre thisGenre = new Genre(item.getGenre());
                    genreIt.insertNext(thisGenre);
                    thisGenre.movieList.add(item);
                    break;
                } else if (g == 0) {
                    MyLinkedListIterator<MovieDBItem> movieIt = new MyLinkedListIterator<>(nextGenre.movieList);
                    while (movieIt.hasNext()) {
                        MovieDBItem nextMovie = movieIt.next();
                        m = item.compareTo(nextMovie);
                        if (m < 0) {
                            movieIt.insertNext(item);
                            break;
                        } else if (m == 0) {
                            break;
                        }
                    }
                    if (m > 0) {
                        nextGenre.movieList.add(item);
                    }
                    break;
                }
            }
            if (g > 0) {
                Genre thisGenre = new Genre(item.getGenre());
                genreList.add(thisGenre);
                thisGenre.movieList.add(item);
            }
        } else {
            Genre thisGenre = new Genre(item.getGenre());
            genreList.add(thisGenre);
            thisGenre.movieList.add(item);
        }
    }
    // FIXME implement this
    // Insert the given item to the MovieDB.
    // Printing functionality is provided for the sake of debugging.
    // This code should be removed before submitting your work.
    // System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());

    public void delete(MovieDBItem item) {
        MyLinkedListIterator<Genre> genreIt = new MyLinkedListIterator<>(genreList);
        while (genreIt.hasNext()) {
            Genre nextGenre = genreIt.next();
            if (item.getGenre().equals(nextGenre.genreName)) {
                MyLinkedListIterator<MovieDBItem> movieIt = new MyLinkedListIterator<>(nextGenre.movieList);
                while (movieIt.hasNext()) {
                    MovieDBItem nextMovie = movieIt.next();
                    if (item.equals(nextMovie)) {
                        movieIt.removeNext();
                        break;
                    }
                }
                if (nextGenre.movieList.numItems == 0) {
                    genreIt.removeNext();
                }
                break;
            }
        }
    }
    // FIXME implement this
    // Remove the given item from the MovieDB.
    // Printing functionality is provided for the sake of debugging.
    // This code should be removed before submitting your work.
    // System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());

    public MyLinkedList<MovieDBItem> search(String term) {
        MyLinkedList<MovieDBItem> searchList = new MyLinkedList<>();
        MyLinkedListIterator<Genre> genreIt = new MyLinkedListIterator<>(genreList);
        while (genreIt.hasNext()) {
            Genre nextGenre = genreIt.next();
            MyLinkedListIterator<MovieDBItem> movieIt = new MyLinkedListIterator<>(nextGenre.movieList);
            while (movieIt.hasNext()){
                MovieDBItem nextMovie = movieIt.next();
                String movieName = nextMovie.getTitle();
                if(movieName.contains(term)){
                    searchList.add(nextMovie);
                }
            }
        }
        return searchList;
    }

    // FIXME implement this
    // Search the given term from the MovieDB.
    // You should return a linked list of MovieDBItem.
    // The search command is handled at SearchCmd class.

    // Printing search results is the responsibility of SearchCmd class.
    // So you must not use System.out in this method to achieve specs of the assignment.

    // This tracing functionality is provided for the sake of debugging.
    // This code should be removed before submitting your work.
    // System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);

    // FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    // This code is supplied for avoiding compilation error.

    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

        // Printing movie items is the responsibility of PrintCmd class.
        // So you must not use System.out in this method to achieve specs of the assignment.

        // Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.

        // FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
        // This code is supplied for avoiding compilation error.
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        MyLinkedListIterator<Genre> genreIt = new MyLinkedListIterator<>(genreList);
        while (genreIt.hasNext()) {
            Genre nextGenre = genreIt.next();
            MyLinkedListIterator<MovieDBItem> movieIt = new MyLinkedListIterator<>(nextGenre.movieList);
            while (movieIt.hasNext()){
                MovieDBItem nextMovie = movieIt.next();
                results.add(nextMovie);
            }
        }
        return results;
    }
}

class Genre implements Comparable<Genre> {
    MyLinkedList<MovieDBItem> movieList;
    String genreName;

    public Genre(String name) {
        this.genreName = name;
        movieList = new MyLinkedList<>();
    }

    @Override
    public int compareTo(Genre o) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}