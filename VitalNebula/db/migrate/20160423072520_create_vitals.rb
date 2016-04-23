class CreateVitals < ActiveRecord::Migration
  def change
    create_table :vitals do |t|
      t.string :device, :null => false
      t.string :sensor, :null => false
      t.integer :value, :null => false
      t.timestamps null: false
    end

    add_index :vitals, :device
    add_index :vitals, :sensor
    add_index :vitals, [:device, :sensor, :created_at], :unique => true
  end
end
