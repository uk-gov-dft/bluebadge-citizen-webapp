SET search_path TO referencedata;

-- England
UPDATE referencedata.reference_data SET meta_data = meta_data::JSONB || '{"differentServiceSignpostUrl": "https://bluebadge.direct.gov.uk/bluebadge/why-are-you-here", "paymentsEnabled": false}' WHERE data_group_id = 'LA' AND code = 'BIRM';

-- Scotland
UPDATE referencedata.reference_data SET meta_data = meta_data::JSONB || '{"paymentsEnabled": false}' WHERE data_group_id = 'LA' AND code = 'ABERD';

-- Wales
UPDATE referencedata.reference_data SET meta_data = meta_data::JSONB || '{"paymentsEnabled": false}' WHERE data_group_id = 'LA' AND code = 'ANGL';
