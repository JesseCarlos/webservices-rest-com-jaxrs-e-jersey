package br.com.alura.loja.test;

import br.com.alura.loja.Servidor;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import com.thoughtworks.xstream.XStream;
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
        String conteudo = target.path(URI_CARRINHOS + "/1").request().get(String.class);
        Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);

        assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
    }

    @Test
    public void deveInserirCarrinho() {
        WebTarget target = client.target(TARGET);

        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        String xml = carrinho.toXml();

        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

        Response response = target.path(URI_CARRINHOS).request().post(entity);
        assertEquals(201, response.getStatus());

        final String conteudo = client.target(response.getHeaderString("Location")).request().get(String.class);
        Assert.assertTrue(conteudo.contains("Tablet"));
    }
}
