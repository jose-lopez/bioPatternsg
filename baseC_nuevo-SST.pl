base([
event('EGF',bind,'EGFR'),
event('EGF',increase,'EGFR'),
event('EGFR',increase,'EGF'),
event('EGF',target,'EGFR'),
event('EGFR',target,'EGF'),
event('EGF',activate,'EGFR'),
event('EGFR',activate,'EGF'),
event('EGF',inhibit,'MEK'),
event('EGF',induce,'MAPK'),
event('EGFR',induce,'MAPK'),
event('EGF',increase,'SOS'),
event('EGFR',increase,'SOS'),
event('EGF',express,'EGFR'),
event('EGFR',express,'EGF'),
event('EGF',inhibit,'EGFR'),
event('EGFR',inhibit,'EGF'),
event('MEK',suppress,'MAPK'),
event('MAPK',mediate,'EGF'),
event('EGF',promote,'MAPK'),
event('EGF',associate,'EGFR'),
event('EGFR',associate,'EGF'),
event('Raf',induce,'MEK'),
event('Ras',suppress,'Raf'),
event('Ras',suppress,'MEK'),
event('EGF',lead,'EGFR'),
event('MEK',mediate,'EGF'),
event('MEK',mediate,'EGFR'),
event('Ras',stimulate,'Raf'),
event('Ras',mediate,'Raf'),
event('Raf',mediate,'Ras'),
event('EGF',trigger,'EGFR'),
event('EGFR',trigger,'EGF'),
event('MEK',decrease,'MAPK'),
event('EGF',bind,'MEK'),
event('EGFR',bind,'EGF'),
event('EGFR',bind,'MEK'),
event('EGFR',enhance,'EGF'),
event('MAPK',activate,'MEK'),
event('MEK',phosphorylate,'MAPK'),
event('Ras',prevent,'Raf'),
event('Ras',prevent,'MEK'),
event('MEK',recognize,'MAPK'),
event('Ras',induce,'MAPK'),
event('MAPK',activate,'CREB'),
event('EGF',activate,'Ras'),
event('EGF',activate,'Raf'),
event('EGF',activate,'MEK'),
event('CREB',target,'MAPK'),
event('MAPK',inhibit,'MEK'),
event('MEK',associate,'MAPK'),
event('Raf',induce,'EGF'),
event('MEK',induce,'EGF'),
event('MAPK',induce,'EGF'),
event('MAPK',induce,'MEK'),
event('MEK',express,'MAPK'),
event('EGF',promote,'EGFR'),
event('EGFR',promote,'EGF'),
event('EGF',suppress,'EGFR'),
event('EGF',suppress,'MAPK'),
event('EGFR',suppress,'EGF'),
event('EGFR',suppress,'MAPK'),
event('MAPK',suppress,'EGF'),
event('MAPK',suppress,'EGFR'),
event('MEK',express,'Raf'),
event('SOS',activate,'Ras'),
event('MEK',inhibit,'MAPK'),
event('SRIF',associate,'MEK'),
event('EGF',induce,'EGFR'),
event('EGFR',induce,'EGF'),
event('Ras',enhance,'Raf'),
event('Raf',enhance,'Ras'),
event('EGF',interact,'EGFR'),
event('EGF',mediate,'EGFR'),
event('EGFR',mediate,'EGF'),
event('CREB',lead,'Ras'),
event('CREB',suppress,'MAPK'),
event('CREB',bind,'MEK'),
event('Ras',involve,'CREB'),
event('MAPK',phosphorylate,'CREB'),
event('MAPK',suppress,'CREB'),
event('EGF',suppress,'CREB'),
event('EGFR',suppress,'CREB'),
event('MAPK',inhibit,'CREB'),
event('EGF',induce,'CREB'),
event('MAPK',involve,'CREB'),
event('CREB',bind,'SRIF'),
event('SRIF',phosphorylate,'CREB'),
event('CREB',target,'SRIF'),
event('EGF',decrease,'EGFR'),
event('EGF',decrease,'CREB'),
event('MAPK',lead,'Ras'),
event('Ras',enhance,'EGF'),
event('EGF',regulate,'EGFR'),
event('EGFR',regulate,'EGF'),
event('EGF',mediate,'MAPK'),
event('CREB',interact,'SRIF'),
event('CREB',bind,'EGF'),
event('MAPK',induce,'CREB'),
event('SRIF',bind,'SSTR5'),
event('SRIF',express,'EGF'),
event('SRIF',target,'EGF'),
event('SRIF',activate,'Ras'),
event('SRIF',involve,'Ras'),
event('EGF',induce,'SRIF'),
event('SRIF',mediate,'SSTR5'),
event('SRIF',decrease,'MEK'),
event('SRIF',decrease,'MAPK'),
event('SSTR5',express,'SRIF'),
event('SSTR5',activate,'SRIF'),
event('SRIF',inhibit,'EGF'),
event('EGF',enhance,'SRIF'),
event('Raf',activate,'SRIF'),
event('MEK',activate,'SRIF'),
event('SRIF',activate,'MAPK'),
event('SRIF',enhance,'CREB'),
event('SRIF',prevent,'EGF'),
event('EGF',regulate,'MAPK'),
event('SRIF',regulate,'EGF'),
event('SRIF',regulate,'EGFR'),
event('SRIF',regulate,'MAPK'),
event('SRIF',enhance,'EGF'),
event('SRIF',enhance,'EGFR'),
event('SRIF',modulate,'EGF'),
event('SRIF',modulate,'EGFR'),
event('EGF',express,'SRIF'),
event('EGFR',express,'SRIF'),
event('EGF',modulate,'MAPK'),
event('EGFR',modulate,'MAPK'),
event('SSTR5',modulate,'MAPK'),
event('SRIF',modulate,'MAPK'),
event('SSTR5',lead,'EGF'),
event('SSTR5',lead,'EGFR'),
event('SSTR5',lead,'SRIF'),
event('SRIF',lead,'EGF'),
event('SRIF',lead,'EGFR'),
event('SRIF',lead,'SSTR5'),
event('SRIF',bind,'EGF'),
event('SRIF',bind,'EGFR'),
event('SRIF',induce,'EGF'),
event('SRIF',induce,'EGFR'),
event('CREB',promote,'SRIF'),
event('SSTR5',mediate,'SRIF'),
event('SRIF',inhibit,'SSTR5'),
event('SRIF',inhibit,'MAPK'),
event('SSTR5',associate,'SRIF'),
event('SRIF',associate,'SSTR5'),
event('SSTR5',increase,'SRIF'),
event('Ras',activate,'SRIF'),
event('Raf',bind,'SRIF'),
event('Ras',mediate,'SRIF'),
event('SRIF',induce,'MAPK'),
event('SSTR5',stimulate,'SRIF'),
event('SRIF',associate,'EGF'),
event('SRIF',trigger,'SSTR5'),
event('SRIF',express,'SSTR5'),
event('SSTR5',bind,'SRIF'),
event('EGF',stimulate,'EGFR'),
event('EGFR',stimulate,'EGF'),
event('CREB',increase,'EGF'),
event('MAPK',inhibit,'EGF'),
event('EGFR',require,'EGF'),
event('Raf',require,'MAPK'),
event('Raf',increase,'EGF'),
event('Raf',increase,'Ras'),
event('EGFR',lead,'EGF'),
event('MEK',target,'EGF'),
event('MEK',target,'EGFR'),
event('EGF',retain,'EGFR'),
event('EGFR',retain,'EGF'),
event('EGF',express,'Ras'),
event('EGF',decrease,'Ras'),
event('MEK',participate,'EGF'),
event('MEK',participate,'EGFR'),
event('MAPK',participate,'EGF'),
event('MAPK',participate,'EGFR'),
event('EGF',phosphorylate,'EGFR'),
event('EGFR',phosphorylate,'EGF'),
event('EGF',enhance,'EGFR'),
event('EGF',prevent,'MAPK'),
event('EGF',recognize,'EGFR'),
event('EGFR',recognize,'EGF'),
event('EGFR',regulate,'MAPK'),
event('EGF',activate,'MAPK'),
event('EGFR',decrease,'EGF'),
event('MAPK',decrease,'EGF'),
event('EGFR',involve,'EGF'),
event('EGFR',reactivate,'EGF'),
event('EGF',target,'MEK'),
event('EGFR',target,'MEK'),
event('EGF',phosphorylate,'MEK'),
event('EGF',induce,'MEK'),
event('EGF',increase,'MAPK'),
event('EGF',involve,'EGFR'),
event('EGFR',prevent,'EGF'),
event('Ras',inhibit,'EGF'),
event('Ras',inhibit,'Raf'),
event('Ras',lead,'MEK'),
event('MAPK',involve,'MEK'),
event('EGFR',interact,'EGF'),
event('MAPK',inhibit,'EGFR'),
event('EGF',stimulate,'MAPK'),
event('EGF',involve,'MAPK'),
event('MAPK',prevent,'EGF'),
event('EGFR',activate,'Ras'),
event('EGFR',activate,'MAPK'),
event('EGF',involve,'CREB'),
event('EGF',recruit,'EGFR'),
event('EGF',prevent,'EGFR'),
event('MAPK',involve,'EGF'),
event('EGF',phosphorylate,'MAPK'),
event('Raf',enhance,'EGF'),
event('EGF',bind,'Raf'),
event('EGF',target,'Raf'),
event('Ras',target,'Raf'),
event('EGF',induce,'Ras'),
event('EGF',induce,'Raf'),
event('Ras',inhibit,'MEK'),
event('EGF',increase,'CREB'),
event('EGF',dimerize,'EGFR'),
event('EGFR',dimerize,'EGF'),
event('MEK',suppress,'EGF'),
event('MEK',suppress,'EGFR'),
event('EGF',lead,'MAPK'),
event('EGF',lead,'CREB'),
event('EGFR',lead,'MAPK'),
event('EGFR',lead,'CREB'),
event('EGF',increase,'Ras'),
event('EGF',increase,'Raf'),
event('EGF',increase,'MEK'),
event('MEK',inhibit,'EGF'),
event('Ras',phosphorylate,'EGF'),
event('MEK',phosphorylate,'EGF'),
event('EGF',regulate,'Ras'),
event('EGF',regulate,'Raf'),
event('EGF',regulate,'MEK'),
event('EGF',decrease,'MEK'),
event('MEK',bind,'EGF'),
event('MEK',bind,'MAPK'),
event('EGF',mediate,'MEK'),
event('EGFR',mediate,'MEK'),
event('MEK',involve,'EGF'),
event('EGFR',recruit,'EGF'),
event('MEK',regulate,'EGF'),
event('MAPK',regulate,'EGF'),
event('MEK',inhibit,'EGFR'),
event('EGFR',regulate,'MEK'),
event('SOS',increase,'EGF'),
event('SOS',increase,'EGFR'),
event('EGF',trigger,'MAPK'),
event('EGF',inhibit,'MAPK'),
event('EGF',activate,'CREB'),
event('MAPK',mediate,'EGFR'),
event('EGFR',phosphorylate,'MAPK'),
event('EGF',prevent,'Ras'),
event('MAPK',require,'EGF'),
event('EGF',require,'EGFR'),
event('Raf',activate,'Ras'),
event('EGF',decrease,'Raf'),
event('EGFR',decrease,'Raf'),
event('EGFR',decrease,'MEK'),
event('EGF',enhance,'MAPK'),
event('EGF',mediate,'Ras'),
event('EGFR',mediate,'Ras'),
event('EGFR',mediate,'MAPK'),
event('EGF',bind,'MAPK'),
event('EGFR',bind,'MAPK'),
event('EGF',increase,'GRB2'),
event('EGF',lead,'Ras'),
event('EGFR',lead,'Ras'),
event('MEK',increase,'EGF'),
event('EGF',enhance,'MEK'),
event('MEK',enhance,'EGF'),
event('EGF',suppress,'MEK'),
event('EGF',require,'MAPK'),
event('MEK',associate,'EGF'),
event('EGF',target,'GRB2'),
event('MEK',activate,'EGF'),
event('MEK',activate,'EGFR'),
event('MAPK',increase,'EGF'),
event('MAPK',lead,'EGF'),
event('MAPK',lead,'EGFR'),
event('EGF',modulate,'EGFR'),
event('EGFR',modulate,'EGF'),
event('EGFR',inhibit,'MAPK'),
event('EGFR',increase,'MAPK'),
event('SOS',involve,'MAPK'),
event('EGF',associate,'MAPK'),
event('MEK',require,'EGF'),
event('MAPK',activate,'EGF'),
event('EGF',prevent,'MEK'),
event('EGFR',prevent,'MEK'),
event('EGF',involve,'Ras'),
event('EGFR',inhibit,'MEK'),
event('Ras',lead,'EGF'),
event('Ras',lead,'EGFR'),
event('EGF',participate,'MAPK'),
event('EGFR',participate,'MAPK'),
event('EGFR',induce,'MEK'),
event('Ras',require,'EGF'),
event('Ras',regulate,'EGF'),
event('EGF',stimulate,'Ras'),
event('EGF',bind,'MNK'),
event('EGFR',bind,'MNK'),
event('MAPK',induce,'EGFR'),
event('EGF',involve,'MEK'),
event('EGF',bind,'Ras'),
event('Ras',stimulate,'EGF'),
event('Ras',stimulate,'EGFR'),
event('Ras',induce,'MEK'),
event('MEK',promote,'EGF'),
event('MEK',promote,'EGFR'),
event('EGFR',increase,'MEK'),
event('EGF',recruit,'Ras'),
event('EGFR',recruit,'Ras'),
event('EGF',enhance,'Ras'),
event('Raf',bind,'Ras'),
event('Raf',lead,'MAPK'),
event('MAPK',target,'EGF'),
event('Ras',activate,'Raf'),
event('Ras',activate,'MEK'),
event('Ras',mediate,'EGF'),
event('Raf',promote,'Ras'),
event('Ras',promote,'Raf'),
event('EGF',retain,'MEK'),
event('GRB2',interact,'EGF'),
event('GRB2',interact,'EGFR'),
event('CREB',mediate,'EGF'),
event('CREB',suppress,'EGF'),
event('CREB',inhibit,'EGF'),
event('MAPK',activate,'EGFR'),
event('EGFR',involve,'Ras'),
event('MEK',phosphorylate,'EGFR'),
event('GRB2',activate,'EGF'),
event('GRB2',activate,'EGFR'),
event('MEK',prevent,'EGF'),
event('EGFR',enhance,'MEK'),
event('EGF',express,'MAPK'),
event('EGFR',express,'MAPK'),
event('Raf',regulate,'Ras'),
event('Raf',regulate,'MEK'),
event('MAPK',bind,'EGF'),
event('MAPK',bind,'Raf'),
event('MAPK',bind,'MEK'),
event('MEK',induce,'EGFR'),
event('Ras',induce,'EGF'),
event('MEK',increase,'EGFR'),
event('Raf',bind,'EGF'),
event('Raf',bind,'MEK'),
event('EGF',increase,'SRIF'),
event('MEK',decrease,'EGF'),
event('EGFR',suppress,'MEK'),
event('EGFR',regulate,'Raf'),
event('EGF',stimulate,'Raf'),
event('EGF',stimulate,'MEK'),
event('EGF',decrease,'MAPK'),
event('EGFR',decrease,'MAPK'),
event('MEK',prevent,'MAPK'),
event('MEK',prevent,'EGFR'),
event('Ras',associate,'EGF'),
event('Ras',associate,'EGFR'),
event('GRB2',lead,'MAPK'),
event('EGFR',enhance,'MAPK'),
event('MEK',enhance,'MAPK'),
event('Raf',target,'EGF'),
event('Raf',target,'EGFR'),
event('EGFR',require,'MAPK'),
event('EGF',mediate,'SRIF'),
event('EGFR',mediate,'SRIF'),
event('EGF',require,'Ras'),
event('EGFR',require,'Ras'),
event('EGF',associate,'Ras'),
event('EGFR',associate,'Ras'),
event('MEK',express,'EGF'),
event('MEK',express,'EGFR'),
event('EGF',involve,'Raf'),
event('EGFR',involve,'Raf'),
event('EGFR',involve,'MEK'),
event('MAPK',associate,'EGF'),
event('MAPK',associate,'EGFR'),
event('EGFR',activate,'MEK'),
event('EGF',inhibit,'Ras'),
event('EGFR',inhibit,'Ras'),
event('MAPK',enhance,'EGF'),
event('MAPK',enhance,'EGFR'),
event('Ras',increase,'MAPK'),
event('MAPK',increase,'EGFR'),
event('EGF',participate,'EGFR'),
event('EGFR',participate,'EGF'),
event('EGFR',induce,'Ras'),
event('EGFR',stimulate,'Ras'),
event('Ras',activate,'EGF'),
event('Ras',activate,'EGFR'),
event('EGFR',increase,'Ras'),
event('EGF',reactivate,'EGFR'),
event('EGFR',decrease,'Ras'),
event('Ras',require,'EGFR'),
event('MAPK',involve,'Ras'),
event('EGFR',induce,'Raf'),
event('MAPK',target,'GRB2'),
event('SOS',bind,'EGF'),
event('SOS',bind,'EGFR'),
event('SOS',bind,'GRB2'),
event('Ras',bind,'EGF'),
event('Ras',bind,'EGFR'),
event('Ras',bind,'GRB2'),
event('Ras',bind,'Raf'),
event('Ras',require,'SOS'),
event('Ras',require,'Raf'),
event('Ras',require,'MEK'),
event('Ras',require,'MAPK'),
event('SOS',lead,'Ras'),
event('SOS',lead,'Raf'),
event('GRB2',increase,'Ras'),
event('GRB2',increase,'Raf'),
event('GRB2',increase,'MEK'),
event('Ras',inhibit,'GRB2'),
event('Ras',promote,'SOS'),
event('GRB2',bind,'EGFR'),
event('EGF',associate,'GRB2'),
event('EGFR',associate,'GRB2'),
event('EGF',regulate,'SOS'),
event('Ras',regulate,'EGFR'),
event('EGFR',bind,'Ras'),
event('GRB2',associate,'Ras'),
event('GRB2',associate,'MAPK'),
event('SOS',associate,'Ras'),
event('SOS',associate,'MAPK'),
event('Ras',associate,'MAPK'),
event('MEK',associate,'Ras'),
event('MAPK',increase,'Ras'),
event('MEK',interact,'EGF'),
event('MEK',interact,'EGFR'),
event('GRB2',bind,'SOS'),
event('MAPK',stimulate,'EGF'),
event('MAPK',promote,'EGF'),
event('SRIF',bind,'GRB2'),
event('Ras',decrease,'Raf'),
event('Ras',increase,'MEK'),
event('Raf',increase,'MEK'),
event('Ras',activate,'SOS'),
event('Raf',mediate,'MAPK'),
event('Raf',associate,'EGF'),
event('Raf',associate,'Ras'),
event('SOS',bind,'Ras'),
event('SOS',bind,'Raf'),
event('Ras',activate,'MAPK'),
event('SOS',recruit,'EGF'),
event('Ras',modulate,'EGF'),
event('MAPK',stimulate,'EGFR'),
event('EGF',trigger,'Ras'),
event('EGF',trigger,'Raf'),
event('SOS',express,'Ras'),
event('MAPK',express,'Ras'),
event('EGF',suppress,'Ras'),
event('Ras',bind,'SOS'),
event('Ras',lead,'MAPK'),
event('MAPK',require,'Ras'),
event('MAPK',require,'Raf'),
event('Ras',bind,'MEK'),
event('MAPK',require,'EGFR'),
event('MAPK',mediate,'GRB2'),
event('Raf',associate,'MEK'),
event('Ras',regulate,'MEK'),
event('EGF',trigger,'GRB2'),
event('EGFR',trigger,'GRB2'),
event('MAPK',activate,'Ras'),
event('EGF',phosphorylate,'Ras'),
event('GRB2',bind,'EGF'),
event('Ras',regulate,'SOS'),
event('MAPK',recruit,'SOS'),
event('EGF',bind,'GRB2'),
event('EGFR',bind,'GRB2'),
event('EGF',stimulate,'GRB2'),
event('EGFR',stimulate,'GRB2'),
event('GRB2',regulate,'Ras'),
event('MAPK',express,'EGF'),
event('MAPK',express,'EGFR'),
event('EGF',trigger,'SOS'),
event('SOS',activate,'Raf'),
event('SOS',activate,'MEK'),
event('EGF',induce,'GRB2'),
event('Ras',lead,'Raf'),
event('Ras',involve,'EGF'),
event('Ras',involve,'EGFR'),
event('Ras',increase,'EGF'),
event('EGF',decrease,'SOS'),
event('EGF',regulate,'GRB2'),
event('GRB2',bind,'Ras'),
event('EGF',stimulate,'SOS'),
event('EGF',activate,'SOS'),
event('EGF',induce,'SOS'),
event('EGF',prevent,'SOS'),
event('Ras',mediate,'SOS'),
event('Ras',inhibit,'SOS'),
event('EGF',promote,'Ras'),
event('SOS',promote,'EGF'),
event('SOS',promote,'EGFR'),
event('SOS',promote,'Ras'),
event('SOS',phosphorylate,'EGF'),
event('SOS',phosphorylate,'Ras'),
event('SOS',phosphorylate,'Raf'),
event('Ras',decrease,'EGF'),
event('Ras',decrease,'SOS'),
event('MAPK',phosphorylate,'SOS'),
event('SOS',associate,'EGF'),
event('SOS',associate,'EGFR'),
event('Ras',involve,'SOS'),
event('MAPK',involve,'Raf'),
event('MEK',induce,'SOS'),
event('MEK',bind,'SOS'),
event('Ras',associate,'SOS'),
event('EGF',associate,'MEK'),
event('EGFR',associate,'MEK'),
event('GRB2',associate,'SOS'),
event('Ras',recruit,'SOS'),
event('Ras',phosphorylate,'GRB2'),
event('EGF',express,'MEK'),
event('SOS',involve,'Ras'),
event('SOS',involve,'Raf'),
event('SOS',involve,'MEK'),
event('EGF',associate,'SOS'),
event('Ras',phosphorylate,'Raf'),
event('SOS',stimulate,'Ras'),
event('GRB2',mediate,'Ras'),
event('Raf',interact,'Ras'),
event('EGF',activate,'GRB2'),
event('EGF',phosphorylate,'GRB2'),
event('EGFR',inhibit,'GRB2'),
event('GRB2',recruit,'EGF'),
event('GRB2',recruit,'EGFR'),
event('GRB2',associate,'EGF'),
event('GRB2',associate,'EGFR'),
event('GRB2',activate,'Ras'),
event('SOS',enhance,'Ras'),
event('Raf',induce,'MAPK'),
event('Ras',inhibit,'MAPK'),
event('EGF',require,'MEK'),
event('EGFR',require,'MEK'),
event('EGF',enhance,'Raf'),
event('Ras',enhance,'MEK'),
event('MEK',activate,'Ras'),
event('MEK',phosphorylate,'Raf'),
event('MAPK',phosphorylate,'Raf'),
event('Ras',stimulate,'MAPK'),
event('Ras',mediate,'MAPK'),
event('MEK',inhibit,'Ras'),
event('MEK',promote,'Ras'),
event('MEK',induce,'Ras'),
event('MEK',induce,'Raf'),
event('MEK',induce,'MAPK'),
event('Raf',involve,'Ras'),
event('Ras',decrease,'MEK'),
event('Ras',mediate,'MEK'),
event('Ras',involve,'MAPK'),
event('MEK',involve,'MAPK'),
event('Ras',retain,'Raf'),
event('Ras',retain,'MEK'),
event('MAPK',decrease,'Ras'),
event('Ras',stimulate,'MEK'),
event('MAPK',inhibit,'Ras'),
event('Raf',require,'Ras'),
event('Ras',regulate,'Raf'),
event('Raf',lead,'MEK'),
event('MAPK',promote,'Raf'),
event('MAPK',phosphorylate,'MEK'),
event('Raf',activate,'MAPK'),
event('Raf',express,'MEK'),
event('EGF',phosphorylate,'Raf'),
event('SOS',associate,'Raf'),
event('Ras',express,'Raf'),
event('MEK',activate,'Raf'),
event('Ras',recruit,'Raf'),
event('MEK',require,'Raf'),
event('Raf',require,'MEK'),
event('Raf',inhibit,'Ras'),
event('Raf',inhibit,'MEK'),
event('MEK',inhibit,'Raf'),
event('Raf',increase,'CREB'),
event('MAPK',regulate,'Raf'),
event('MAPK',regulate,'MEK'),
event('Raf',suppress,'Ras'),
event('Raf',involve,'EGF'),
event('Raf',involve,'EGFR'),
event('Raf',activate,'MEK'),
event('MAPK',mediate,'Raf'),
event('MEK',target,'Raf'),
event('MAPK',bind,'SRIF'),
event('Raf',bind,'MAPK'),
event('Raf',stimulate,'MEK'),
event('Ras',induce,'SOS'),
event('EGFR',activate,'Raf'),
event('MAPK',activate,'Raf'),
event('EGF',mediate,'Raf'),
event('Raf',stimulate,'MAPK'),
event('EGF',target,'Ras'),
event('EGF',target,'MAPK'),
event('MAPK',induce,'Ras'),
event('EGF',express,'Raf'),
event('Ras',interact,'Raf'),
event('MAPK',inhibit,'Raf'),
event('Raf',phosphorylate,'MEK'),
event('Raf',decrease,'MEK'),
event('Raf',decrease,'MAPK'),
event('MAPK',increase,'Raf'),
event('Raf',target,'MEK'),
event('MEK',regulate,'Raf'),
event('EGFR',modulate,'MEK'),
event('MAPK',mediate,'MEK'),
event('MEK',target,'Ras'),
event('MEK',suppress,'Raf'),
event('Ras',target,'MEK'),
event('MEK',increase,'Raf'),
event('Ras',associate,'MEK'),
event('Ras',interact,'MEK'),
event('MEK',bind,'Raf'),
event('Raf',promote,'MEK'),
event('EGFR',promote,'MAPK'),
event('MAPK',promote,'EGFR'),
event('MEK',activate,'MAPK'),
event('MAPK',decrease,'EGFR'),
event('Ras',induce,'Raf'),
event('MAPK',express,'MEK'),
event('MEK',require,'MAPK'),
event('CREB',require,'MAPK'),
event('MEK',regulate,'MAPK'),
event('Raf',induce,'Ras'),
event('Raf',lead,'EGF'),
event('MEK',lead,'Raf'),
event('CREB',regulate,'MAPK'),
event('MAPK',require,'MEK'),
event('Raf',recognize,'Ras'),
event('Raf',phosphorylate,'Ras'),
event('Raf',phosphorylate,'MAPK'),
event('Raf',involve,'MAPK'),
event('EGF',trigger,'MEK'),
event('MAPK',increase,'MEK'),
event('MEK',decrease,'CREB'),
event('MEK',require,'EGFR'),
event('MEK',interact,'Raf'),
event('MEK',increase,'MAPK'),
event('Ras',involve,'MEK'),
event('Raf',recognize,'MEK'),
event('MEK',recognize,'Raf'),
event('SRIF',prevent,'MAPK'),
event('Raf',prevent,'MEK'),
event('MAPK',bind,'MNK'),
event('MEK',express,'Ras'),
event('Raf',mediate,'MEK'),
event('Ras',express,'MEK'),
event('Raf',stimulate,'EGF'),
event('MEK',stimulate,'Raf'),
event('MEK',mediate,'Raf'),
event('Raf',inhibit,'MAPK'),
event('MAPK',participate,'Ras'),
event('MAPK',participate,'Raf'),
event('MAPK',participate,'MEK'),
event('Raf',decrease,'Ras'),
event('Raf',stimulate,'Ras'),
event('Ras',increase,'Raf'),
event('Raf',interact,'MEK'),
event('Raf',lead,'SOS'),
event('MEK',reactivate,'MAPK'),
event('MAPK',reactivate,'MEK'),
event('MEK',lead,'MAPK'),
event('Ras',participate,'MAPK'),
event('Raf',participate,'MAPK'),
event('MEK',participate,'MAPK'),
event('Ras',phosphorylate,'MEK'),
event('Raf',regulate,'MAPK'),
event('SRIF',activate,'EGF'),
event('EGF',involve,'SRIF'),
event('SOS',stimulate,'SRIF'),
event('EGF',inhibit,'Raf'),
event('MAPK',modulate,'EGF'),
event('MEK',modulate,'EGF'),
event('MEK',modulate,'EGFR'),
event('MEK',modulate,'MAPK'),
event('Ras',prevent,'EGF'),
event('Ras',express,'EGF'),
event('Ras',inhibit,'EGFR'),
event('Ras',promote,'EGF'),
event('MAPK',enhance,'Ras'),
event('EGFR',associate,'MAPK'),
event('EGFR',stimulate,'MEK'),
event('MAPK',prevent,'MEK'),
event('MEK',lead,'EGF'),
event('MEK',lead,'EGFR'),
event('EGFR',phosphorylate,'MEK'),
event('EGF',lead,'MEK'),
event('EGFR',lead,'MEK'),
event('MAPK',target,'EGFR'),
event('Ras',involve,'Raf'),
event('MAPK',express,'Raf'),
event('Raf',induce,'CREB'),
event('Raf',suppress,'MEK'),
event('Raf',increase,'MAPK'),
event('Raf',target,'Ras'),
event('Raf',bind,'CREB'),
event('MEK',bind,'CREB'),
event('MAPK',prevent,'Raf'),
event('Raf',target,'MAPK'),
event('Raf',decrease,'EGF'),
event('Raf',express,'MAPK'),
event('Raf',recruit,'Ras'),
event('EGF',suppress,'Raf'),
event('EGFR',suppress,'Ras'),
event('EGFR',suppress,'Raf'),
event('Raf',inhibit,'EGF'),
event('MEK',regulate,'Ras'),
event('MAPK',regulate,'Ras'),
event('Raf',express,'Ras'),
event('Ras',modulate,'MAPK'),
event('Raf',recruit,'MEK'),
event('Raf',promote,'MAPK'),
event('Ras',decrease,'MAPK'),
event('MEK',require,'Ras'),
event('MAPK',induce,'Raf'),
event('MEK',involve,'Raf'),
event('MAPK',bind,'Ras'),
event('MEK',prevent,'Raf'),
event('Raf',phosphorylate,'EGF'),
event('MAPK',trigger,'Ras'),
event('MAPK',trigger,'Raf'),
event('Ras',express,'MAPK'),
event('MAPK',stimulate,'SRIF'),
event('CREB',phosphorylate,'EGF'),
event('CREB',phosphorylate,'EGFR'),
event('CREB',phosphorylate,'Raf'),
event('CREB',target,'EGF'),
event('CREB',target,'EGFR'),
event('EGF',require,'Raf'),
event('MAPK',promote,'Ras'),
event('Raf',involve,'MEK'),
event('MEK',stimulate,'Ras'),
event('Raf',prevent,'Ras'),
event('Ras',target,'EGF'),
event('MEK',enhance,'Raf'),
event('Raf',trigger,'Ras'),
event('MEK',trigger,'Ras'),
event('Raf',activate,'EGF'),
event('Ras',enhance,'MAPK'),
event('MEK',prevent,'Ras'),
event('Raf',retain,'Ras'),
event('Ras',trigger,'Raf'),
event('Ras',trigger,'MEK'),
event('MAPK',stimulate,'Ras'),
event('Ras',associate,'Raf'),
event('Raf',prevent,'EGF'),
event('EGF',lead,'Raf'),
event('SRIF',mediate,'Ras'),
event('SRIF',mediate,'Raf'),
event('SOS',participate,'Ras'),
event('EGFR',lead,'Raf'),
event('Ras',prevent,'MAPK'),
event('MEK',associate,'Raf'),
event('MAPK',stimulate,'Raf'),
event('MEK',stimulate,'EGF'),
event('Ras',promote,'MEK'),
event('Ras',suppress,'MAPK'),
event('Raf',suppress,'MAPK'),
event('MAPK',suppress,'Ras'),
event('MEK',mediate,'MAPK'),
event('SRIF',require,'Ras'),
event('SRIF',require,'Raf'),
event('Raf',associate,'MAPK'),
event('SRIF',stimulate,'Raf'),
event('MAPK',modulate,'Raf'),
event('MAPK',modulate,'MEK'),
event('Raf',prevent,'MAPK'),
event('SRIF',express,'Ras'),
event('EGF',prevent,'Raf'),
event('EGFR',involve,'MAPK'),
event('MAPK',lead,'MEK'),
event('Ras',modulate,'Raf'),
event('MAPK',enhance,'Raf'),
event('MAPK',enhance,'MEK'),
event('Raf',reactivate,'MAPK'),
event('Raf',require,'EGF'),
event('SOS',decrease,'Raf'),
event('SOS',decrease,'MAPK'),
event('MEK',involve,'Ras'),
event('Ras',regulate,'MAPK'),
event('Raf',induce,'EGFR'),
event('Raf',enhance,'MEK'),
event('MEK',mediate,'Ras'),
event('Ras',participate,'EGF'),
event('Ras',phosphorylate,'MAPK'),
event('Raf',modulate,'Ras'),
event('Raf',mediate,'EGF'),
event('MEK',bind,'Ras'),
event('MAPK',target,'MEK'),
event('MAPK',target,'Ras'),
event('MEK',decrease,'Ras'),
event('MEK',target,'MAPK'),
event('CREB',involve,'SOS'),
event('CREB',involve,'EGF'),
event('CREB',phosphorylate,'MAPK'),
event('MAPK',bind,'CREB'),
event('MAPK',decrease,'CREB'),
event('CREB',decrease,'MAPK'),
event('MEK',increase,'CREB'),
event('CREB',increase,'MEK'),
event('MAPK',associate,'MEK'),
event('MEK',lead,'Ras'),
event('MAPK',decrease,'MEK'),
event('MEK',prevent,'CREB'),
event('MNK',bind,'MAPK'),
event('Raf',induce,'MNK'),
event('MEK',induce,'MNK'),
event('MEK',stimulate,'MAPK'),
event('EGFR',trigger,'MAPK'),
event('MAPK',trigger,'EGF'),
event('MAPK',trigger,'EGFR'),
event('MAPK',target,'Raf'),
event('MAPK',stimulate,'MEK'),
event('Ras',target,'MAPK'),
event('MAPK',associate,'Ras'),
event('SOS',decrease,'Ras'),
event('MEK',suppress,'Ras'),
event('CREB',decrease,'MEK'),
event('MEK',promote,'CREB'),
event('Raf',enhance,'MAPK'),
event('MEK',retain,'MAPK'),
event('MEK',enhance,'Ras'),
event('EGFR',stimulate,'MAPK'),
event('MAPK',lead,'CREB'),
event('EGFR',prevent,'MAPK'),
event('MAPK',suppress,'MEK'),
event('EGFR',stimulate,'Raf'),
event('CREB',activate,'MAPK'),
event('MEK',promote,'Raf'),
event('MAPK',increase,'CREB'),
event('EGF',stimulate,'CREB'),
event('EGFR',stimulate,'CREB'),
event('CREB',mediate,'MEK'),
event('CREB',mediate,'MAPK'),
event('CREB',inhibit,'MAPK'),
event('MEK',involve,'CREB'),
event('MEK',lead,'CREB'),
event('MEK',increase,'Ras'),
event('CREB',promote,'MEK'),
event('MAPK',prevent,'EGFR'),
event('MEK',regulate,'CREB'),
event('MAPK',regulate,'CREB'),
event('Ras',participate,'MEK'),
event('EGF',modulate,'Ras'),
event('EGFR',modulate,'Ras'),
event('MAPK',modulate,'EGFR'),
event('MAPK',modulate,'Ras'),
event('Ras',bind,'MAPK'),
event('MAPK',mediate,'CREB'),
event('MAPK',target,'CREB'),
event('Ras',participate,'Raf'),
event('MEK',inhibit,'CREB'),
event('EGFR',decrease,'CREB'),
event('MEK',modulate,'Raf'),
event('EGF',modulate,'MEK'),
event('MEK',interact,'MAPK'),
event('Ras',lead,'SOS'),
event('Raf',lead,'Ras'),
event('SOS',retain,'Ras'),
event('MAPK',regulate,'EGFR'),
event('MAPK',require,'SRIF'),
event('CREB',involve,'MEK'),
event('Ras',recognize,'Raf'),
event('CREB',induce,'MAPK'),
event('MAPK',interact,'MEK'),
event('EGF',transcribe,'EGFR'),
event('EGFR',transcribe,'EGF'),
event('SRIF',stimulate,'MAPK'),
event('MEK',activate,'CREB'),
event('Ras',trigger,'EGF'),
event('MEK',decrease,'Raf')
]).
