(ns conversor.formatar)

(defn extrai-Variacao [data]
  (try
    (- (get-in data ["regularMarketPreviousClose"]) (get-in data ["regularMarketOpen"]))
    (catch Exception ex
      (println "ocorreu um erro ao pegar a variacao"))
    )
  )

(defn extrai-Variacao-percent [data]
  (try
    (* 100 (/
    (- (get-in data ["regularMarketPreviousClose"]) (get-in data ["regularMarketOpen"]))
    (get-in data ["regularMarketPreviousClose"])))
    (catch Exception ex
      (println "ocorreu um erro ao pegar a variacao percentual"))
    )
  )

(defn filtrar-tipo [resposta]
  (let [tipo-ativo (re-find #"PN|ON" resposta)]
    (when tipo-ativo tipo-ativo) ))

(defn valida-descricao [descricao]
  (try
    (if (= descricao "{}")
    "O ativo nao tem descricao" descricao) 
    (catch Exception ex
      (println "Ocorreu um erro ao pegar a descricao")
      (println ex))
    )
  )

(defn formata-detalhes [data-brapi descricao]
  (try
  (let [data (get data-brapi 0)]
    (str (format
      "Nome: %s\nCodigo: %s\nTipo de ativo: %s\nDescricao: %s\nVariacao do dia: %.2f\nVariacao do dia em percentual: %.2f\nUltimo Preco: %f\nPreco Maximo: %f\nPreco Minimo: %f\nPreco de Abertura: %f\nPreco de fechamento: %f\nHora: %s"
      (get-in data ["longName"])
      (get-in data ["symbol"])
      (filtrar-tipo (get-in data ["shortName"]))
      (valida-descricao descricao)
      (extrai-Variacao data)
      (extrai-Variacao-percent data)
      (float (get-in data ["regularMarketPrice"]))
      (float (get-in data ["regularMarketDayHigh"]))
      (float (get-in data ["regularMarketDayLow"]))
      (float (get-in data ["regularMarketOpen"]))
      (float (get-in data ["regularMarketPreviousClose"]))
      (get-in data ["regularMarketTime"])
      )))
    (catch Exception ex
      (println "Ocorreu uma excecao ao formatar os detalhes:")
      (println ex)
      (println "Ainda estamos trabalhando em uma soluçao"))))


(defn filtro-name [acao]
  (str (format "Symbol: %s Name: %s"
               (get-in acao ["stock"])
               (get-in acao ["name"]))))
(defn formata-lista-brapi [ data ]
  (map filtro-name data))

(defn formata-saldo [saldo]
  (format "Saldo %s" saldo))
