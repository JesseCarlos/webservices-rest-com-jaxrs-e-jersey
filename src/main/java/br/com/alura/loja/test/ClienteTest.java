package br.com.alura.loja.test;

import br.com.alura.loja.Servidor;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class ClienteTest {

    public static final String URI_CARRINHOS = "/carrinhos";
    public static final String TARGET = "http://localhost:8080";
    private HttpServer servidor;
    private Client client;

    @Before
    public void startaServidor() {
        servidor = Servidor.inicializaServidor();
        ClientConfig config = new ClientConfig();
        config.register(new LoggingFilter());
        client = ClientBuilder.newClient(config);
    }

    @After
    public void mataServidor() {
        servidor.stop();
    }

    @Test
    public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
        final WebTarget target = client.target(TARGET);
        Carrinho carrinho = target.path(URI_CARRINHOS + "/1").request().get(Carrinho.class);
        assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
    }

    @Test
    public void deveInserirCarrinho() {
        WebTarget target = client.target(TARGET);

        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");

        Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

        Response response = target.path(URI_CARRINHOS).request().post(entity);
        assertEquals(201, response.getStatus());

        final Carrinho carrinhoCarregado = client.target(response.getHeaderString("Location")).request().get(Carrinho.class);
        Assert.assertEquals("Tablet", carrinhoCarregado.getProdutos().get(0).getNome());
    }
}
