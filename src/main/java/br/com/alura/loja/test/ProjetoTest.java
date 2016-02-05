package br.com.alura.loja.test;

import br.com.alura.loja.Servidor;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Projeto;
import com.thoughtworks.xstream.XStream;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class ProjetoTest {

    private HttpServer servidor;

    @Before
    public void startaServidor() {
        servidor = Servidor.inicializaServidor();
    }

    @After
    public void mataServidor() {
        servidor.stop();
    }

    @Test
    public void testaQueBuscarUmProjetoTrazOProjetoEsperado() {
        Client client = ClientBuilder.newClient();
        final WebTarget target = client.target("http://localhost:8080");
        String conteudo = target.path("/projetos").request().get(String.class);
        Projeto projeto = (Projeto) new XStream().fromXML(conteudo);

        Assert.assertEquals("Minha loja", projeto.getNome());
    }
}
