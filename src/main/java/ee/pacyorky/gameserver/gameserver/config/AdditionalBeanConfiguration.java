package ee.pacyorky.gameserver.gameserver.config;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import ee.pacyorky.gameserver.gameserver.agoraio.client.AgoraClient;

/**
 * Date: 30.11.2021
 * Time: 13:19
 */
@Configuration
public class AdditionalBeanConfiguration {
    
    private static final int CONNECT_TIMEOUT = 30000;
    
    private static final int REQUEST_TIMEOUT = 30000;
    
    private static final int SOCKET_TIMEOUT = 60000;
    
    @Bean
    public AgoraClient agoraClient(ObjectFactory<RestTemplate> restTemplateFactory, AgoraProperties agoraProperties) {
        return new AgoraClient(restTemplateFactory, agoraProperties);
    }
    
    @Bean
    @Scope("prototype")
    public RestTemplate restTemplate(AgoraProperties agoraProperties) {
        return new RestTemplate(clientHttpRequestFactory(agoraProperties));
    }
    
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory(AgoraProperties agoraProperties) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient(agoraProperties));
        return clientHttpRequestFactory;
    }
    
    @Bean
    @Scope("prototype")
    public CloseableHttpClient httpClient(AgoraProperties agoraProperties) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
        var credProvider = new BasicCredentialsProvider();
        var creds = new UsernamePasswordCredentials(agoraProperties.getAgoraUser(), agoraProperties.getAgoraPassword());
        credProvider.setCredentials(AuthScope.ANY, creds);
        return HttpClients.custom()
                .setDefaultCredentialsProvider(credProvider)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
    
}
