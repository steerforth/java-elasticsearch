package com.steer.javaelasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class EsConfig {
    @Value("${elasticsearch.uris}")
    private String uris;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = uris.split(",");
        for (String addr : hostList) {
            hostLists.add(HttpHost.create(addr));
        }

        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[] {});
        RestClientBuilder clientBuilder = RestClient.builder(httpHost);
        clientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
//            @SneakyThrows
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
                    credentialsProvider.setCredentials(AuthScope.ANY, credentials);
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
                IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(100)
                        .setConnectTimeout(10000).setSoTimeout(10000).build();
                httpClientBuilder.setDefaultIOReactorConfig(ioReactorConfig);
                SSLContext sslContext = null;
                try {
                    sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1) {
                            return true;
                        }
                    }).build();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (KeyManagementException e) {
                    throw new RuntimeException(e);
                } catch (KeyStoreException e) {
                    throw new RuntimeException(e);
                }
                httpClientBuilder.setSSLContext(sslContext);
                httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                return httpClientBuilder;
            }
        });
        return new RestHighLevelClient(clientBuilder);
    }

}
