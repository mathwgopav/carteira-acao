(ns conversor.cambista
  (:require [clj-http.client :as http-client]
            [cheshire.core :refer [parse-string generate-string]]
            [conversor.formatar :as formatar]))


; AlphaVantage
(def chave "U9M6M8NUUZ440R6A")

(def urldescricao
  "https://www.alphavantage.co/query?function=OVERVIEW")


(defn obter-descricao [symbol]
  (-> (:body (http-client/get urldescricao
                              {:query-params {"symbol" (str symbol ".sao") "apikey" chave}}))))

; Brapi
(def urllista
  "https://brapi.dev/api/quote/list")

(def urlticket
  "https://brapi.dev/api/quote/")

(defn obter-acoes-brapi []
  (try
    (mapv println  (-> (:body (http-client/get urllista))
      (parse-string)
      (get-in ["stocks"])
      (formatar/formata-lista-brapi)))
  (catch Exception ex
    (println "Ocorreu uma exceção ao obter a lista de ações:")
    (println ex))))

(defn detalhar-acao-brapi [symbol]
  (try
    (-> (:body (http-client/get (str urlticket symbol)))
        (parse-string)
        (get-in ["results"]))
    (catch Exception ex
      (println (str "Ocorreu uma exceção ao detalhar a ação " symbol ":"))
      (println ex))))