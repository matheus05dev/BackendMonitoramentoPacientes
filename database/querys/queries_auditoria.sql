SELECT 
       COALESCE(u.username, 'Sistema') AS username,
       COUNT(*) AS total_acoes
FROM monitoramentopacientedb.log l
LEFT JOIN monitoramentopacientedb.user u ON u.id = l.user_id
GROUP BY COALESCE(u.username, 'Sistema')
ORDER BY total_acoes DESC;

SELECT event_type,
       COUNT(*) AS total
FROM monitoramentopacientedb.log
GROUP BY event_type
ORDER BY total DESC;

SELECT 
    CASE WHEN user_id IS NULL THEN 'Sistema' ELSE 'Humano' END AS origem,
    COUNT(*) AS total
FROM monitoramentopacientedb.log
GROUP BY origem;

SELECT HOUR(timestamp) AS hora, COUNT(*)
FROM monitoramentopacientedb.log
GROUP BY hora;

SELECT event_type,
       COUNT(*) AS total
FROM monitoramentopacientedb.log
WHERE timestamp >= NOW() - INTERVAL 7 DAY
GROUP BY event_type
ORDER BY total DESC;

SELECT *
FROM monitoramentopacientedb.log
WHERE timestamp >= NOW() - INTERVAL 1 HOUR
ORDER BY timestamp DESC;

SELECT DATE(timestamp) AS dia, COUNT(*) AS total
FROM monitoramentopacientedb.log
GROUP BY dia
ORDER BY dia DESC;

SELECT event_type, COUNT(*) AS total
FROM monitoramentopacientedb.log
WHERE event_type IN ('TENTATIVA_AUTENTICACAO', 'SUCESSO_AUTENTICACAO', 'LOGOUT')
GROUP BY event_type;

SELECT *
FROM monitoramentopacientedb.log
WHERE user_id = 1
ORDER BY timestamp DESC;

SELECT *
FROM monitoramentopacientedb.log
WHERE event_type LIKE 'SALVAR_LEITURA_SENSOR%'
ORDER BY timestamp DESC;

SELECT 
    id,
    timestamp,
    LAG(timestamp) OVER (ORDER BY timestamp) AS anterior,
    TIMESTAMPDIFF(SECOND, LAG(timestamp) OVER (ORDER BY timestamp), timestamp) AS diff_segundos
FROM monitoramentopacientedb.log;

SELECT DATE_FORMAT(timestamp, '%Y-%m-%d %H:%i') AS minuto,
       COUNT(*) AS total
FROM monitoramentopacientedb.log
GROUP BY minuto
ORDER BY minuto DESC;

SELECT event_type,
       COUNT(*) AS total
FROM monitoramentopacientedb.log
GROUP BY event_type
ORDER BY total DESC
LIMIT 5;