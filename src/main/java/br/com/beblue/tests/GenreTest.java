package br.com.beblue.tests;

import br.com.beblue.apis.GenresApi;
import org.junit.Assert;
import org.junit.Test;

public class GenreTest {
    
    @Test
    public void list() {
        
        GenresApi genresApi = new GenresApi();        
        
        Assert.assertTrue(genresApi.genres().equals(1));
        
    }
    
}
