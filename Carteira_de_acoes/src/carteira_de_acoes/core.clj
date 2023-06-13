(ns carteira-de-acoes.core
  (:require [conversor.interpretador-de-opcoes :refer [interpretar-opcoes]]
            [clj-http.client :as client]
            [conversor.formatar :refer [formata-detalhes formata-saldo]]
            [conversor.cambista :as cambista]))

(defn exibe-detalhes []
  (println "Escreva o nome da acao que voce quer ver!")
  (let [acao (read-string (read-line))]
    (println
      (formata-detalhes
        (cambista/detalhar-acao-brapi acao)
        (cambista/obter-descricao acao)))))

(defn menu [opcao]
  (println)
  (if
    (= opcao 3) (println "Obrigado por usar nossos servicos")
    (do
      (println "Bem vindo a sua carteira de acoes.")
      (println "Escolha o que deseja fazer.")
      (println "1- Listar Acoes da bolsa.")
      (println "2- Exibir os detalhes de um acao.")
      (println "3- Encerrar.")
      (let [opcao (read-string (read-line))]
        (do
          (cond
            (= opcao 1) (cambista/obter-acoes-brapi)
            (= opcao 2) (exibe-detalhes)
            (= opcao 8) ""
            :else (println "Opcao invalida"))
          (println)
          (menu opcao))))))


(defn -main
    [& args]
    (menu 0))